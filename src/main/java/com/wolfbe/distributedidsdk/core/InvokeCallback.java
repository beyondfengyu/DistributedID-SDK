package com.wolfbe.distributedidsdk.core;

import com.wolfbe.distributedidsdk.sdk.ResponseFuture;

/**
 * @author Andy
 */
public interface InvokeCallback {

    void operationComplete(ResponseFuture responseFuture);
}
