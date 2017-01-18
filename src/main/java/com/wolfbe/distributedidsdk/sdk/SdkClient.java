package com.wolfbe.distributedidsdk.sdk;


import com.wolfbe.distributedidsdk.util.GlobalConfig;
import com.wolfbe.distributedidsdk.util.NettyUtil;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andy
 */
public class SdkClient extends AbstractClient {

    protected AtomicInteger rqid = new AtomicInteger(0);

    @Override
    public void start() {
        b.group(workGroup)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("SdkServerDecoder", new SdkClientDecoder(12))
                                .addLast("SdkServerEncoder", new SdkClientEncoder())
                                .addLast("SdkClientHandler", new SdkClientHandler());
                    }
                });
        try {
            cf = b.connect(GlobalConfig.DEFAULT_HOST, GlobalConfig.SDKS_PORT).sync();
            cf.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    logger.info("client channel close");
                    shutdown();
                }
            });

            InetSocketAddress address = (InetSocketAddress) cf.channel().remoteAddress();
            logger.info("SdkClient start success, host is {}, port is {}", address.getHostName(),
                    address.getPort());
        } catch (InterruptedException e) {
            logger.error("SdkClient start error", e);
            shutdown(); //关闭并释放资源
        }
    }

    class SdkClientHandler extends SimpleChannelInboundHandler<SdkProto> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, SdkProto sdkProto) throws Exception {
            final int rqid = sdkProto.getRqid();
            final ResponseFuture responseFuture = asyncResponse.get(sdkProto.getRqid());
            if (responseFuture != null) {
                responseFuture.setSdkProto(sdkProto);
                responseFuture.release();
                asyncResponse.remove(rqid);

                // 异步请求，执行回调函数
                if (responseFuture.getInvokeCallback() != null) {
                    responseFuture.executeInvokeCallback();
                }else{
                    // 同步请求，返回数据并释放CountDown
                    responseFuture.putResponse(sdkProto);
                }
            }else{
                logger.warn("receive response, but not matched any request,, ", NettyUtil.parseRemoteAddr(ctx.channel()));
                logger.warn("response data is {}",sdkProto.toString());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("SdkHandler error", cause);
            NettyUtil.closeChannel(ctx.channel());
        }


    }

}
