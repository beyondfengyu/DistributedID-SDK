package com.wolfbe.distributedidsdk;

import com.wolfbe.distributedidsdk.exception.RemotingConnectException;
import com.wolfbe.distributedidsdk.exception.RemotingTimeoutException;
import com.wolfbe.distributedidsdk.exception.RemotingTooMuchRequestException;
import com.wolfbe.distributedidsdk.sdk.SdkClient;

/**
 * @author Andy
 */
public class SdkStartup {


    public static void main(String[] args) throws InterruptedException, RemotingTimeoutException,
            RemotingConnectException, RemotingTooMuchRequestException {
        SdkClient client = new SdkClient();
        client.init();
        client.start();
        for (int i = 0; i < 2000; i++) {

            client.invokeAsync(null,2000, null);
//            TimeUnit.SECONDS.sleep(2);
        }
    }

    /**
     * @author Andy
     */
    public static class ClientStartup {
    }
}
