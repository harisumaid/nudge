package com.example.nudge.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderModel implements Parcelable {
    String orderedFarmerName;
    String orderedDate;
    String orderedProductName;
    String orderedProductId;
    String orderedFarmerId;
    String orderedId;
    String orderedReceivingDate;
    String orderedDeliveryDate;

    public OrderModel(String orderedFarmerName, String orderedProductId, String orderedDate, String orderedFarmerId, String orderedId, String orderedReceivingDate, String orderedDeliveryDate,String orderedProductName){
        this.orderedFarmerName = orderedFarmerName;
        this.orderedProductId = orderedProductId;
        this.orderedDate = orderedDate;
        this.orderedFarmerId = orderedFarmerId;
        this.orderedId = orderedId;
        this.orderedDeliveryDate = orderedDeliveryDate;
        this.orderedReceivingDate=orderedReceivingDate;
        this.orderedProductName=orderedProductName;
    }

    protected OrderModel(Parcel in) {
        orderedFarmerName = in.readString();
        orderedDate = in.readString();
        orderedProductId = in.readString();
        orderedFarmerId = in.readString();
        orderedId = in.readString();
        orderedReceivingDate = in.readString();
        orderedDeliveryDate = in.readString();
        orderedProductName = in.readString();
    }

    public OrderModel(){

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderedFarmerName);
        dest.writeString(orderedDate);
        dest.writeString(orderedProductId);
        dest.writeString(orderedFarmerId);
        dest.writeString(orderedId);
        dest.writeString(orderedDeliveryDate);
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

    public void setOrderedProductName(String orderedProductName) {
        this.orderedProductName = orderedProductName;
    }

    public String getOrderedProductName() {
        return orderedProductName;
    }

    public String getOrderedId() {
        return orderedId;
    }

    public void setOrderedId(String orderedId) {
        this.orderedId = orderedId;
    }

    public String getOrderedFarmerName() {
        return orderedFarmerName;
    }

    public String getOrderedProductId() {
        return orderedProductId;
    }

    public String getOrderedDate(){
//        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.ENGLISH);
//        Date date = simpleDateFormat.parse(orderedDate);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
        return orderedDate.substring(0,10)+", "+orderedDate.substring(orderedDate.lastIndexOf(" "));
    }

    public void setOrderedFarmerName(String orderedFarmerName) {
        this.orderedFarmerName = orderedFarmerName;
    }

    public void setOrderedFarmerId(String orderedFarmerId) {
        this.orderedFarmerId = orderedFarmerId;
    }

    public String getOrderedFarmerId() {
        return orderedFarmerId;
    }

    public void setOrderedProductId(String orderedProductId) {
        this.orderedProductId = orderedProductId;
    }

    public void setOrderedReceivingDate(String orderedReceivingDate) {
        this.orderedReceivingDate = orderedReceivingDate;
    }

    public void setOrderedDeliveryDate(String orderedDeliveryDate) {
        this.orderedDeliveryDate = orderedDeliveryDate;
    }

    public String getOrderedReceivingDate() {
        if (orderedReceivingDate!=null)
        return orderedReceivingDate.substring(0,10)+", "+orderedReceivingDate.substring(orderedReceivingDate.lastIndexOf(" "));
        else
            return orderedReceivingDate;
    }

    public String getOrderedDeliveryDate() {
        if (orderedDeliveryDate!=null)
        return orderedDeliveryDate.substring(0,10)+", "+orderedDeliveryDate.substring(orderedDeliveryDate.lastIndexOf(" "));
        else
            return orderedDeliveryDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

}
