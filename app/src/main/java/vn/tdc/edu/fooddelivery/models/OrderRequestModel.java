package vn.tdc.edu.fooddelivery.models;

import com.google.gson.annotations.SerializedName;

public class OrderRequestModel {
    @SerializedName("id")
    private Integer id;
    @SerializedName("shipper_id")
    private Integer shipperId;
    @SerializedName("status")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
