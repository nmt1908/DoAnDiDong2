package vn.tdc.edu.fooddelivery.models;

public class ItemCartModel {

    private Integer id;
    private Integer user_id;
    private Integer product_id;
    private Integer quantity;

    public Integer getUser_id() {
        return user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
