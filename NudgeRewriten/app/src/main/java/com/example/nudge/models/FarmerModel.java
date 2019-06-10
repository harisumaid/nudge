package com.example.nudge.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class FarmerModel implements Parcelable {

    String name,primary_contact_number,secondary_contact_number,image,address,farm_size,id;
    GeoPoint farm_location;

    public FarmerModel() {
    }

    protected FarmerModel(Parcel in) {
        name = in.readString();
        primary_contact_number = in.readString();
        secondary_contact_number = in.readString();
        image = in.readString();
        address = in.readString();
        farm_size = in.readString();
        id = in.readString();
    }

    public static final Creator<FarmerModel> CREATOR = new Creator<FarmerModel>() {
        @Override
        public FarmerModel createFromParcel(Parcel in) {
            return new FarmerModel(in);
        }

        @Override
        public FarmerModel[] newArray(int size) {
            return new FarmerModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimary_contact_number() {
        return primary_contact_number;
    }

    public void setPrimary_contact_number(String primary_contact_number) {
        this.primary_contact_number = primary_contact_number;
    }

    public String getSecondary_contact_number() {
        return secondary_contact_number;
    }

    public void setSecondary_contact_number(String secondary_contact_number) {
        this.secondary_contact_number = secondary_contact_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFarm_size() {
        return farm_size;
    }

    public void setFarm_size(String farm_size) {
        this.farm_size = farm_size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeoPoint getFarm_location() {
        return farm_location;
    }

    public void setFarm_location(GeoPoint farm_location) {
        this.farm_location = farm_location;
    }

    public FarmerModel(String name, String primary_contact_number, String secondary_contact_number, String image, String address, String farm_size, String id, GeoPoint farm_location) {
        this.name = name;
        this.primary_contact_number = primary_contact_number;
        this.secondary_contact_number = secondary_contact_number;
        this.image = image;
        this.address = address;
        this.farm_size = farm_size;
        this.id = id;
        this.farm_location = farm_location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(primary_contact_number);
        dest.writeString(secondary_contact_number);
        dest.writeString(image);
        dest.writeString(address);
        dest.writeString(farm_size);
        dest.writeString(id);
    }
}
