package com.wolfbe.distributedidsdk;

import com.wolfbe.distributedidsdk.core.InvokeCallback;
import com.wolfbe.distributedidsdk.exception.RemotingConnectException;
import com.wolfbe.distributedidsdk.exception.RemotingSendRequestException;
import com.wolfbe.distributedidsdk.exception.RemotingTimeoutException;
import com.wolfbe.distributedidsdk.exception.RemotingTooMuchRequestException;
import com.wolfbe.distributedidsdk.sdk.ResponseFuture;
import com.wolfbe.distributedidsdk.sdk.SdkClient;
import com.wolfbe.distributedidsdk.sdk.SdkProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 同步请求压测
 * @author Andy
 */
public class PressTestSync {
    private static final Logger logger = LoggerFactory.getLogger("PressTestSync");
    private static int NUM = 60000;

    public static void main(String[] args) throws InterruptedException, RemotingTimeoutException,
            RemotingConnectException, RemotingTooMuchRequestException, RemotingSendRequestException {
        SdkClient client = new SdkClient();
        client.init();
        client.start();

        long start = 0;
        long end = 0;
        long cast = 0;
        long amount = 0;
        long allcast = 0;

        for(int k = 0; k < 20; k++) {
            start = System.currentTimeMillis();
            for (int i = 0; i < NUM; i++) {
                final SdkProto sdkProto = new SdkProto();
                client.invokeSync(sdkProto, 5000);
            }

            end = System.currentTimeMillis();
            cast = (end - start) ;
            allcast += cast;

            logger.info("invokeSync test num is: {}, cast time: {} millsec, throughput: {} send/millsec",
                    NUM, cast, (double)NUM/cast);

            amount += NUM;
            NUM += 5000;
            TimeUnit.SECONDS.sleep(2);
        }

        logger.info("invokeSync test all num is: {}, all cast time: {} millsec, all throughput: {} send/millsec",
                amount, allcast, amount/allcast);
    }
}
