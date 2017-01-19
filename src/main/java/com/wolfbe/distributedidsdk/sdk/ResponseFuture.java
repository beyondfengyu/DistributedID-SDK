package com.wolfbe.distributedidsdk.sdk;

import com.wolfbe.distributedidsdk.core.InvokeCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Andy
 */
public class ResponseFuture {
    private final int rqid;
    private final long timeoutMillis;
    private final Semaphore semaphore;
    private final InvokeCallback invokeCallback;
    private final AtomicBoolean executeCallbackOnlyOnce = new AtomicBoolean(false);
    private final AtomicBoolean semaphoreReleaseOnlyOnce = new AtomicBoolean(false);
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private volatile Throwable cause;
    private volatile SdkProto sdkProto;
    private volatile boolean isSendStateOk;

    public ResponseFuture(int rqid, long timeoutMillis, InvokeCallback invokeCallback,
                          Semaphore semaphore) {
        this.rqid = rqid;
        this.timeoutMillis = timeoutMillis;
        this.invokeCallback = invokeCallback;
        this.semaphore = semaphore;
        this.isSendStateOk = false;
    }

    /**
     * 流量控制时，释放获取的信号量
     */
    public void release() {
        if (this.semaphore != null) {
            if (semaphoreReleaseOnlyOnce.compareAndSet(false, true)) {
                this.semaphore.release();
            }
        }
    }

    public void executeInvokeCallback() {
        if (invokeCallback != null) {
            if (executeCallbackOnlyOnce.compareAndSet(false, true)) {
                invokeCallback.operationComplete(this);
            }
        }
    }

    public SdkProto waitResponse(final long timeoutMillis) throws InterruptedException {
        this.countDownLatch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        return this.sdkProto;
    }

    /**
     * 同步请求时，释放阻塞的CountDown
     *
     * @param sdkProto
     */
    public void putResponse(SdkProto sdkProto) {
        this.sdkProto = sdkProto;
        this.countDownLatch.countDown();
    }

    public int getRqid() {
        return rqid;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public InvokeCallback getInvokeCallback() {
        return invokeCallback;
    }

    public AtomicBoolean getExecuteCallbackOnlyOnce() {
        return executeCallbackOnlyOnce;
    }

    public AtomicBoolean getSemaphoreReleaseOnlyOnce() {
        return semaphoreReleaseOnlyOnce;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public SdkProto getSdkProto() {
        return sdkProto;
    }

    public void setSdkProto(SdkProto sdkProto) {
        this.sdkProto = sdkProto;
    }

    public boolean isSendStateOk() {
        return isSendStateOk;
    }

    public void setIsSendStateOk(boolean isSendStateOk) {
        this.isSendStateOk = isSendStateOk;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "ResponseFuture{" +
                "rqid=" + rqid +
                ", timeoutMillis=" + timeoutMillis +
                ", sdkProto=" + sdkProto +
                ", isSendStateOk=" + isSendStateOk +
                '}';
    }
}
