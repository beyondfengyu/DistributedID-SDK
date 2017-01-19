package com.wolfbe.distributedidsdk;

import com.wolfbe.distributedidsdk.core.InvokeCallback;
import com.wolfbe.distributedidsdk.exception.RemotingConnectException;
import com.wolfbe.distributedidsdk.exception.RemotingSendRequestException;
import com.wolfbe.distributedidsdk.exception.RemotingTimeoutException;
import com.wolfbe.distributedidsdk.exception.RemotingTooMuchRequestException;
import com.wolfbe.distributedidsdk.sdk.ResponseFuture;
import com.wolfbe.distributedidsdk.sdk.SdkClient;
import com.wolfbe.distributedidsdk.sdk.SdkProto;

/**
 * @author Andy
 */
public class SdkStartup {
    private static final int NUM = 2000;

    public static void main(String[] args) throws InterruptedException, RemotingTimeoutException,
            RemotingConnectException, RemotingTooMuchRequestException, RemotingSendRequestException {
        SdkClient client = new SdkClient();
        client.init();
        client.start();
        long start = System.currentTimeMillis();
        for (int i = 0; i < NUM; i++) {
            SdkProto sdkProto = new SdkProto();
            System.out.println("sendProto: " + sdkProto.toString());
            SdkProto resultProto = client.invokeSync(sdkProto, 2000);
            System.out.println(i+" resultProto: " + resultProto.toString());
        }
        long end = System.currentTimeMillis();
        System.out.println("invokeSync test num is: "+NUM+", cast time: "+(end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < NUM; i++) {
            SdkProto sdkProto = new SdkProto();
            System.out.println("sendProto: " + sdkProto.toString());
            final int finalI = i;
            client.invokeAsync(sdkProto, 2000, new InvokeCallback() {
                @Override
                public void operationComplete(ResponseFuture responseFuture) {
                    System.out.println(finalI +" resultProto: " + responseFuture.getSdkProto().toString());
                }
            });
        }
        end = System.currentTimeMillis();
        System.out.println("invokeSync test num is: "+NUM+", cast time: "+(end - start));

    }

    /**
     * @author Andy
     */
    public static class ClientStartup {
    }
}
