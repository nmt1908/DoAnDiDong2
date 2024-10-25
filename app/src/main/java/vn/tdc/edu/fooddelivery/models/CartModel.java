package vn.tdc.edu.fooddelivery.models;

import com.google.gson.annotations.SerializedName;

public class CartModel {

    @SerializedName("cart_id")
    private Integer cart_id;

    @SerializedName("product")
    private ProductModel product;

    @SerializedName("user")
    private UserModel user;

    @SerializedName("quantity")
    private Integer quantity;

    public Integer getCart_id() {
        return cart_id;
    }

    public void setCart_id(Integer cart_id) {
        this.cart_id = cart_id;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
