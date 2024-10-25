package vn.tdc.edu.fooddelivery.models;

import com.google.gson.annotations.SerializedName;

public class FileModel {

    @SerializedName("image")
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
