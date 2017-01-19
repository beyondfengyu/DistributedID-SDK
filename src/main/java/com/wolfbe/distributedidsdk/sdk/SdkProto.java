package com.wolfbe.distributedidsdk.sdk;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andy
 */
public class SdkProto {

    private static AtomicInteger requestId = new AtomicInteger(0);

    private int rqid = requestId.incrementAndGet(); //请求的ID
    private long did; //全局的ID


    public static SdkProto createSdkProto(int rqid, long did) {
        SdkProto sdkProto = new SdkProto();
        sdkProto.setDid(did);
        sdkProto.setRqid(rqid);
        return sdkProto;
    }

    public int getRqid() {
        return rqid;
    }

    public void setRqid(int rqid) {
        this.rqid = rqid;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    @Override
    public String toString() {
        return "SdkProto{" +
                "rqid=" + rqid +
                ", did=" + did +
                '}';
    }
}
