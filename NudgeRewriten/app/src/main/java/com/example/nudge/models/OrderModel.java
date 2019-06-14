package com.example.nudge.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class OrderModel implements Comparable<OrderModel> {
    String orderedFarmerName;
    String orderedProductName;
    String orderedProductId;
    String orderedFarmerId;
    String orderedId;

    private @ServerTimestamp Date orderedDate;
    private Date orderedReceivingDate;
    private Date orderedDeliveryDate;

    public OrderModel() {
    }

    protected OrderModel(Parcel in) {
        orderedFarmerName = in.readString();
        orderedProductName = in.readString();
        orderedProductId = in.readString();
        orderedFarmerId = in.readString();
        orderedId = in.readString();
    }

    public static final Parcelable.Creator<OrderModel> CREATOR = new Parcelable.Creator<OrderModel>() {
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

    public void setOrderedFarmerName(String orderedFarmerName) {
        this.orderedFarmerName = orderedFarmerName;
    }

    public String getOrderedProductName() {
        return orderedProductName;
    }

    public void setOrderedProductName(String orderedProductName) {
        this.orderedProductName = orderedProductName;
    }

    public String getOrderedProductId() {
        return orderedProductId;
    }

    public void setOrderedProductId(String orderedProductId) {
        this.orderedProductId = orderedProductId;
    }

    public String getOrderedFarmerId() {
        return orderedFarmerId;
    }

    public void setOrderedFarmerId(String orderedFarmerId) {
        this.orderedFarmerId = orderedFarmerId;
    }

    public String getOrderedId() {
        return orderedId;
    }

    public void setOrderedId(String orderedId) {
        this.orderedId = orderedId;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Date getOrderedReceivingDate() {
        return orderedReceivingDate;
    }

    public void setOrderedReceivingDate(Date orderedReceivingDate) {
        this.orderedReceivingDate = orderedReceivingDate;
    }

    public Date getOrderedDeliveryDate() {
        return orderedDeliveryDate;
    }

    public void setOrderedDeliveryDate(Date orderedDeliveryDate) {
        this.orderedDeliveryDate = orderedDeliveryDate;
    }

    public OrderModel(String orderedFarmerName, String orderedProductName, String orderedProductId, String orderedFarmerId, String orderedId, Date orderedDate, Date orderedReceivingDate, Date orderedDeliveryDate) {
        this.orderedFarmerName = orderedFarmerName;
        this.orderedProductName = orderedProductName;
        this.orderedProductId = orderedProductId;
        this.orderedFarmerId = orderedFarmerId;
        this.orderedId = orderedId;
        this.orderedDate = orderedDate;
        this.orderedReceivingDate = orderedReceivingDate;
        this.orderedDeliveryDate = orderedDeliveryDate;
    }

    @Override
    public int compareTo(OrderModel o) {
        return getOrderedDate().compareTo(o.getOrderedDate());
    }
}
