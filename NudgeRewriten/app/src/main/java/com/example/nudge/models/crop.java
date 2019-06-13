package com.example.nudge.models;

import android.os.Parcel;
import android.os.Parcelable;

public class crop {

    private String name,image,desc,id;

    public crop() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public crop(String name, String image, String desc, String id) {
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.id = id;
    }
}