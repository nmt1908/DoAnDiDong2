package vn.tdc.edu.fooddelivery.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.tdc.edu.fooddelivery.constant.SystemConstant;

public class ProductModel extends BaseModel {
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("quantity")
    private Integer quantity;
    @SerializedName("price")
    private Long price;
    @SerializedName("description")
    private String description;

    @SerializedName("type")
    private String unit;

    @SerializedName("categoryIds")
    private List<Integer> categoryIds;

    private double rating = 3.0f;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return image;
    }

    public String getImageUrl() {
        return SystemConstant.IMAGES_BASE_URL + image;
    }

    public void setImageName(String image) {
        this.image = image;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
