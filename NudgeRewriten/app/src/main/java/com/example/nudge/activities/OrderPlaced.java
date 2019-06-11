package com.example.nudge.activities;

import android.app.Application;

import com.example.nudge.models.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderPlaced extends Application {
    private List<OrderModel> list;

    public OrderPlaced() {
        this.list = new ArrayList<>();
    }

    public void addOrderPlaced(List<OrderModel> list)
    {
        for(OrderModel orderModel: list)
        {
            this.list.add(orderModel);
        }
    }
    public List<OrderModel> getOrderPlaced(){
        return this.list;
    }

}
