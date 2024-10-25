package vn.tdc.edu.fooddelivery.models;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.tdc.edu.fooddelivery.constant.SystemConstant;

public class CategoryModel extends BaseModel {
    private static List<CategoryModel> categoriesList;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;

    @SerializedName("numberOfProduct")
    private Integer numberOfProduct;

    public String getImageUrl() {
        return SystemConstant.IMAGES_BASE_URL + image;
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

    public CategoryModel setImageName(String image) {
        this.image = image;
        return this;
    }

    public Integer getNumberOfProduct() {
        return numberOfProduct;
    }

    public void setNumberOfProduct(Integer numberOfProduct) {
        this.numberOfProduct = numberOfProduct;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
