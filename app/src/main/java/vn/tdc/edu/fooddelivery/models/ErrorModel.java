package vn.tdc.edu.fooddelivery.models;

import com.google.gson.annotations.SerializedName;

public class ErrorModel {
    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
