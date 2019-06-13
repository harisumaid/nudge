package com.example.nudge.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderModel implements Parcelable {
    String orderedFarmerName;
    String orderedReceivingDate;
    String orderedProductName;

    public OrderModel(String orderedFarmerName, String orderedProductName, String orderedReceivingDate){
        this.orderedFarmerName = orderedFarmerName;
        this.orderedProductName = orderedProductName;
        this.orderedReceivingDate = orderedReceivingDate;
    }

    protected OrderModel(Parcel in) {
        orderedFarmerName = in.readString();
        orderedReceivingDate = in.readString();
        orderedProductName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderedFarmerName);
        dest.writeString(orderedReceivingDate);
        dest.writeString(orderedProductName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    public String getOrderedFarmerName() {
        return orderedFarmerName;
    }

    public String getOrderedProductName() {
        return orderedProductName;
    }

    public String getOrderedReceivingDate() {
        return orderedReceivingDate;
    }

    public void setOrderedFarmerName(String orderedFarmerName) {
        this.orderedFarmerName = orderedFarmerName;
    }

    public void setOrderedProductName(String orderedProductName) {
        this.orderedProductName = orderedProductName;
    }

    public void setOrderedReceivingDate(String orderedReceivingDate) {
        this.orderedReceivingDate = orderedReceivingDate;
    }

}
