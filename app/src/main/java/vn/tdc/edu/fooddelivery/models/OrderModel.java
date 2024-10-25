package vn.tdc.edu.fooddelivery.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderModel extends BaseModel {
    @SerializedName("user")
    private UserModel customer;

    @SerializedName("user_id")
    private Integer userId;

    @SerializedName("shipper")
    private UserModel shipper;

    @SerializedName("shipper_id")
    private Integer shipperId;

    @SerializedName("total")
    private Long total;

    @SerializedName("delivery_address")
    private String address;

    @SerializedName("customer_phone")
    private String phone;

    @SerializedName("status")
    private Integer status;

    @SerializedName("items")
    private List<OrderItemModel> items;

    public UserModel getCustomer() {
        return customer;
    }

    public void setCustomer(UserModel customer) {
        this.customer = customer;
    }

    public UserModel getShipper() {
        return shipper;
    }

    public void setShipper(UserModel shipper) {
        this.shipper = shipper;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OrderItemModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemModel> items) {
        this.items = items;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }
}
