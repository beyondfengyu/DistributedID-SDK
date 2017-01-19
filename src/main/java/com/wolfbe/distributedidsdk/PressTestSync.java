package com.wolfbe.distributedidsdk;

import com.wolfbe.distributedidsdk.core.InvokeCallback;
import com.wolfbe.distributedidsdk.exception.RemotingConnectException;
import com.wolfbe.distributedidsdk.exception.RemotingSendRequestException;
import com.wolfbe.distributedidsdk.exception.RemotingTimeoutException;
import com.wolfbe.distributedidsdk.exception.RemotingTooMuchRequestException;
import com.wolfbe.distributedidsdk.sdk.ResponseFuture;
import com.wolfbe.distributedidsdk.sdk.SdkClient;
import com.wolfbe.distributedidsdk.sdk.SdkProto;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 同步请求压测
 * @author Andy
 */
public class PressTestSync {
    private static final int NUM = 2000;

    public static void main(String[] args) throws InterruptedException, RemotingTimeoutException,
            RemotingConnectException, RemotingTooMuchRequestException, RemotingSendRequestException {
        SdkClient client = new SdkClient();
        client.init();
        client.start();
        long start = System.currentTimeMillis();
//        for (int i = 0; i < NUM; i++) {
//            SdkProto sdkProto = new SdkProto();
////            System.out.println(i+" sendProto: " + sdkProto.toString());
//            SdkProto resultProto = client.invokeSync(sdkProto, 2000);
////            System.out.println(i+" resultProto: " + resultProto.toString());
//        }
        long end = System.currentTimeMillis();
        long cast = (end -start)/1000 + 1;
        System.out.println("invokeSync test num is: " + NUM + ", cast time: " + cast+"s, throughput: "+NUM/cast+" send/sec");

        final CountDownLatch countDownLatch = new CountDownLatch(NUM);
        start = System.currentTimeMillis();
        for (int i = 0; i < NUM; i++) {
            final SdkProto sdkProto = new SdkProto();
            final int finalI = i;
            client.invokeAsync(sdkProto, 2000, new InvokeCallback() {
                @Override
                public void operationComplete(ResponseFuture responseFuture) {
                    System.out.println(finalI + " sendProto: " + sdkProto.toString());
                    countDownLatch.countDown();
                    System.out.println(finalI + " resultProto: " + responseFuture.getSdkProto().toString());
                }
            });
        }
        end = System.currentTimeMillis();
        cast = (end -start);
        countDownLatch.await(10, TimeUnit.SECONDS);
        System.out.println("invokeAsync test num is: " + NUM + ", cast time: " + cast+"s, throughput: "+ 1000* NUM/cast+" send/sec");


    }
}
