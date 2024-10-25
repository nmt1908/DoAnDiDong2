package vn.tdc.edu.fooddelivery.models;

import com.google.gson.annotations.SerializedName;

public class NotificationModel extends BaseModel {

    @SerializedName("title")
    String title;

    @SerializedName("image")
    String image;


    @SerializedName("content")
    String content;

    @SerializedName("user_id")
    int user_id;

    @SerializedName("status")
    int status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
