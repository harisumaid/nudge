package com.example.nudge.models;

import java.util.Date;

public class CropModel {

    String cropname,cropVariety,cropFarmSize,cropImage,id;
    Date seedingDate;

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getCropVariety() {
        return cropVariety;
    }

    public void setCropVariety(String cropVariety) {
        this.cropVariety = cropVariety;
    }

    public String getCropFarmSize() {
        return cropFarmSize;
    }

    public void setCropFarmSize(String cropFarmSize) {
        this.cropFarmSize = cropFarmSize;
    }

    public String getCropImage() {
        return cropImage;
    }

    public void setCropImage(String cropImage) {
        this.cropImage = cropImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getSeedingDate() {
        return seedingDate;
    }

    public void setSeedingDate(Date seedingDate) {
        this.seedingDate = seedingDate;
    }

    public CropModel() {
    }

    public CropModel(String cropname, String cropVariety, String cropFarmSize, String cropImage, String id, Date seedingDate) {
        this.cropname = cropname;
        this.cropVariety = cropVariety;
        this.cropFarmSize = cropFarmSize;
        this.cropImage = cropImage;
        this.id = id;
        this.seedingDate = seedingDate;
    }
}
