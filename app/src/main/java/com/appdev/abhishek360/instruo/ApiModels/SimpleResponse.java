package com.appdev.abhishek360.instruo.ApiModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleResponse {
    @Expose
    @SerializedName("success")
    private boolean success = true;
    @Expose
    @SerializedName("message")
    private String msg;
    @Expose
    @SerializedName("accessToken")
    private String accessToken = "a360";
    @Expose
    @SerializedName("order_id")
    private String orderId = "gshhkkjs-sjhbhs";
    @Expose
    @SerializedName("url")
    private String paymentUrl = "xyz";

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
