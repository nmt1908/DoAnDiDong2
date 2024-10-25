package vn.tdc.edu.fooddelivery.models;

import com.google.gson.annotations.SerializedName;

public class OrderItemModel {
    @SerializedName("product")
    private ProductModel productModel;
    @SerializedName("quantity")
    private Integer quantity;
    @SerializedName("price")
    private Long price;
    @SerializedName("sub_total")
    private Long subTotal;

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Long subTotal) {
        this.subTotal = subTotal;
    }
}
