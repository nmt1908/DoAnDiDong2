package vn.tdc.edu.fooddelivery.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class BaseModel {
    @SerializedName("id")
    private Integer id;
    @SerializedName("created_at")
    private Timestamp createdAt;
    @SerializedName("updated_at")
    private Timestamp updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
