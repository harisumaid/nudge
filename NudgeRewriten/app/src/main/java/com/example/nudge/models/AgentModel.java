package com.example.nudge.models;

public class AgentModel {

    String id,device,farmers_count,image,level,name,phone_number;
    Boolean isLoggedIn;
    Integer points;

    public AgentModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getFarmers_count() {
        return farmers_count;
    }

    public void setFarmers_count(String farmers_count) {
        this.farmers_count = farmers_count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Boolean getLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public AgentModel(String id, String device, String farmers_count, String image, String level, String name, String phone_number, Boolean isLoggedIn, Integer points) {
        this.id = id;
        this.device = device;
        this.farmers_count = farmers_count;
        this.image = image;
        this.level = level;
        this.name = name;
        this.phone_number = phone_number;
        this.isLoggedIn = isLoggedIn;
        this.points = points;
    }
}
