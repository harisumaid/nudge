package com.example.nudge.models;

import android.os.Parcel;
import android.os.Parcelable;

public class products implements Parcelable {

    String number_available,product_company,product_id,product_image,product_name;
    boolean availability;
    int product_price;

    public products(String number_available,String product_company,String product_id,String product_image,String product_name,boolean availability, int product_price){
        this.availability=availability;
        this.number_available = number_available;
        this.product_company = product_company;
        this.product_id = product_id;
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_price = product_price;
    }

    public products(){

    }

    protected products(Parcel in) {
        number_available = in.readString();
        product_company = in.readString();
        product_id = in.readString();
        product_image = in.readString();
        product_name = in.readString();
        availability = in.readByte() != 0;
        product_price = in.readInt();
    }

    public static final Creator<products> CREATOR = new Creator<products>() {
        @Override
        public products createFromParcel(Parcel in) {
            return new products(in);
        }

        @Override
        public products[] newArray(int size) {
            return new products[size];
        }
    };

    public String getNumber_available() {
        return number_available;
    }

    public void setNumber_available(String number_available) {
        this.number_available = number_available;
    }

    public String getProduct_company() {
        return product_company;
    }

    public void setProduct_company(String product_company) {
        this.product_company = product_company;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number_available);
        dest.writeString(product_company);
        dest.writeString(product_id);
        dest.writeString(product_image);
        dest.writeString(product_name);
        dest.writeByte((byte) (availability ? 1 : 0));
        dest.writeInt(product_price);
    }
}