package com.wolfbe.distributedidsdk.core;

import com.wolfbe.distributedidsdk.exception.RemotingConnectException;
import com.wolfbe.distributedidsdk.exception.RemotingSendRequestException;
import com.wolfbe.distributedidsdk.exception.RemotingTimeoutException;
import com.wolfbe.distributedidsdk.exception.RemotingTooMuchRequestException;
import com.wolfbe.distributedidsdk.sdk.SdkProto;


/**
 * @author Andy
 */
public interface Client {

    void start();

    void shutdown();

    SdkProto invokeSync(SdkProto proto, long timeoutMillis) throws RemotingConnectException, RemotingTimeoutException, InterruptedException, RemotingSendRequestException;

    void invokeAsync(SdkProto proto, long timeoutMillis, InvokeCallback invokeCallback) throws RemotingConnectException,
            RemotingTooMuchRequestException, RemotingTimeoutException, InterruptedException, RemotingSendRequestException;

    void invokeOneWay(SdkProto proto, long timeoutMillis) throws RemotingConnectException, RemotingTooMuchRequestException,
            RemotingTimeoutException, InterruptedException, RemotingSendRequestException;
}
