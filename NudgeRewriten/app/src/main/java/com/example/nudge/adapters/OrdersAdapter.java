package com.example.nudge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nudge.R;
import com.example.nudge.activities.OrdersActivity;
import com.example.nudge.models.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.myViewHolder> {

    List<OrderModel> orderedModels,receivedOrderModels,deliveredOrderModels,modelList;
    List<Integer> flag;
    Context context;
    static int currentPosition = -1;
    Map<String,String> imageMap = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> name = new ArrayList<>();

    public OrdersAdapter(List<OrderModel> orderedModels,List<OrderModel> receivedOrderModels,List<OrderModel> deliveredOrderModels, List<Integer>flag, Context context, Map<String,String> imageMap) {
        this.orderedModels = orderedModels;
        this.receivedOrderModels = receivedOrderModels;
        this.deliveredOrderModels = deliveredOrderModels;
        this.flag = flag;
        this.context = context;
        this.imageMap = imageMap;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.orders_fertilizer_card, viewGroup, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrdersAdapter.myViewHolder myViewHolder, final int i) {

        Glide.with(context).load(imageMap.get(modelList.get(i).getOrderedProductId())).into(myViewHolder.productImage);


        myViewHolder.farmerName.setText(modelList.get(i).getOrderedFarmerName());

        myViewHolder.visitTitle.setText(modelList.get(i).getOrderedProductName());



        if (flag.get(0)==0) {           //for ordered tab
            myViewHolder.type.setText("Order for :");
            myViewHolder.checksvg.setVisibility(View.GONE);
            myViewHolder.orderBtn.setVisibility(View.GONE);
            myViewHolder.orderDate.setText((modelList.get(i).getOrderedDate()));

        }
        if (flag.get(0)==1) {           //for received tab
            myViewHolder.type.setText("Received for :");
            myViewHolder.checksvg.setVisibility(View.VISIBLE);
            myViewHolder.orderBtn.setVisibility(View.VISIBLE);
            myViewHolder.orderBtn.setText("Click here if delivered");
            myViewHolder.orderDate.setText((modelList.get(i).getOrderedReceivingDate()));
            myViewHolder.checksvg.setImageResource(R.drawable.ic_baseline_check_circle_24px);
            myViewHolder.datesvg.setImageResource(R.drawable.ic_local_shipping_black);
            myViewHolder.orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrdersActivity ordersActivity = (OrdersActivity)context;
                    receivedOrderModels.get(i).setOrderedDeliveryDate(String.valueOf(Calendar.getInstance().getTime()));
                    deliveredOrderModels.add(receivedOrderModels.get(i));
                    ordersActivity.uploadDeliveryDate(receivedOrderModels.get(i));
                    receivedOrderModels.remove(i);
                    notifyDataSetChanged();
                }
            });

        }


        if (flag.get(0)==2){            //for delivered tab

            myViewHolder.type.setText("Delivered to :");
            myViewHolder.orderBtn.setVisibility(View.VISIBLE);
            myViewHolder.checksvg.setVisibility(View.VISIBLE);
            myViewHolder.datesvg.setImageResource(R.drawable.ic_local_shipping_shipped_24dp);
            myViewHolder.checksvg.setImageResource(R.drawable.ic_check_circle_checked_24dp);
            myViewHolder.orderDate.setText((modelList.get(i).getOrderedDeliveryDate()));
            myViewHolder.orderBtn.setText("Product Delivered");

        }

        myViewHolder.childView.setVisibility(View.GONE);

        if(currentPosition == i) {
            myViewHolder.childView.setVisibility(View.VISIBLE);
        }



        myViewHolder.orderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPosition == i) currentPosition = -1;
                else
                currentPosition = i;
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        if (flag.get(0)==0){
            modelList = orderedModels;
            Log.d("click", "flag 0: "+modelList.size());
        }
        else if (flag.get(0)==1){
            modelList = receivedOrderModels;
            Log.d("click", "flag 1: "+modelList.size());
        }
        else {
            modelList = deliveredOrderModels;
            Log.d("click", "flag 2: "+modelList.size());
        }
        Log.d("click", "getItemCount: "+modelList.size());
        return modelList.size();

    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView farmerName,type,orderDate,orderBtn,visitTitle;
        ConstraintLayout childView;
        CardView orderCard;
        ImageView  checksvg,datesvg,productImage;

        public myViewHolder(@NonNull View itemView) {

            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            datesvg= itemView.findViewById(R.id.date_svg);
            checksvg = itemView.findViewById(R.id.check_svg);
            visitTitle = itemView.findViewById(R.id.visit_title);
            farmerName = itemView.findViewById(R.id.farmer_name);
            type = itemView.findViewById(R.id.type);
            orderDate = itemView.findViewById(R.id.visit_date);
            orderBtn = itemView.findViewById(R.id.order_btn);
            childView = itemView.findViewById(R.id.visit_child_view);
            orderCard = itemView.findViewById(R.id.visit_order_card);
        }
    }
//    public  void updateAdapter(List<OrderModel> list)
//    {
//        for(OrderModel orderModel: list)
//        {
//            this.farmers.add(orderModel.getOrderedFarmerName());
//            this.dates.add(orderModel.getOrderedDate());
//            this.orderTypes.add("Ordered By:");
//            this.flag.add(1);
//        }
//        notifyDataSetChanged();
//    }
}
