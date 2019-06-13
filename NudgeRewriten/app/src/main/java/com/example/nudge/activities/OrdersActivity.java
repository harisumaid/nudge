package com.example.nudge.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.adapters.OrdersAdapter;
import com.example.nudge.models.OrderModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersActivity extends AppCompatActivity {


    Button orderBtn,receivedBtn,deliveredBtn;
    ImageView backBtn;
    RecyclerView orderRcv;
    OrdersAdapter adapter;
    ProgressBar orderProgressBar;
    public TextView ifOrderEmpty;
    List<OrderModel> orderModels = new ArrayList<>();
    List<OrderModel> orderModelList; //Shared Predference list
    List<OrderModel> orderedList = new ArrayList<>();
    Map<String,String> imageMap = new HashMap<>();
    List<OrderModel> receivedList = new ArrayList<>();
    List<OrderModel> deliveredList = new ArrayList<>();
    SharedPreferences preferences;
    FirebaseFirestore db;
    SharedPrefUtils sharedPrefUtils;
    Context context;
    List<Integer> flag = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        context = this;
        sharedPrefUtils = new SharedPrefUtils(context);

        db = FirebaseFirestore.getInstance(); //instantiate firestore
        loadFirestore(); //load firestore

        ifOrderEmpty = findViewById(R.id.if_order_empty);

        orderBtn = findViewById(R.id.order_btn);
        orderProgressBar = findViewById(R.id.order_progress_bar);
        receivedBtn = findViewById(R.id.received_btn);
        deliveredBtn = findViewById(R.id.delivered_btn);
        backBtn = findViewById(R.id.back_btn1);
        orderRcv = findViewById(R.id.order_rcv);
        preferences = getSharedPreferences("MyPref",MODE_PRIVATE);

        orderRcv.setVisibility(View.GONE);
        orderProgressBar.setVisibility(View.VISIBLE);

        orderRcv.setLayoutManager(new LinearLayoutManager(this));
        orderRcv.setItemAnimator(new DefaultItemAnimator());
        flag.add(0);

        adapter=new OrdersAdapter(orderedList,receivedList,deliveredList,flag,context,imageMap);



        orderBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) v.performClick();
            }
        });

        receivedBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) v.performClick();
            }
        });

        deliveredBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) v.performClick();
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderBtn.requestFocus();
                flag.clear();
                flag.add(0);
                if (orderedList.size()==0) {
                    ifOrderEmpty.setVisibility(View.VISIBLE);
//                    ifOrderEmpty.setText("No Order Placed");
                }
                else
                    ifOrderEmpty.setVisibility(View.GONE);


                adapter.notifyDataSetChanged();
//                loadSharedPref();
            }
        });

        receivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receivedBtn.requestFocus();
                flag.clear();
                flag.add(1);

                if (receivedList.size()==0) {
                    ifOrderEmpty.setVisibility(View.VISIBLE);
                    ifOrderEmpty.setText("Nothing to show");
                }
                else
                    ifOrderEmpty.setVisibility(View.GONE);


                adapter.notifyDataSetChanged();
            }
        });

        deliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveredBtn.requestFocus();
                flag.clear();
                flag.add(2);

                if (deliveredList.size()==0) {
                    ifOrderEmpty.setText("Nothing to show");
                    ifOrderEmpty.setVisibility(View.VISIBLE);
                }
                else
                    ifOrderEmpty.setVisibility(View.GONE);


                adapter.notifyDataSetChanged();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public  void  loadSharedPref(){
        Gson gson = new Gson();
        String json = preferences.getString("OrderPlaced",null);
        Type type = new TypeToken<ArrayList<OrderModel>>() {}.getType();
        orderModelList= gson.fromJson(json,type);
        if(orderModelList == null)
        {
            orderModelList.add(new OrderModel("Name","ProductName","OrderDate","FarmerId","OrderId","OrderReceivingDate","OrderDeliveryDate","OrderProductName"));
        }
        Log.d("sharedpref", "loadSharedPref: "+orderModelList.get(0));
   //     adapter.updateAdapter(orderModelList);
        adapter.notifyDataSetChanged();
    }

    public void loadFirestore(){
        db.collection("agents")
                .document(sharedPrefUtils.readAgentId())
                .collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                if (documentSnapshot.exists()) {
                                    OrderModel model = documentSnapshot.toObject(OrderModel.class);
                                    Log.d("click", "onComplete: "+model.getOrderedReceivingDate());
                                    if (model.getOrderedReceivingDate()==null) {
                                        orderedList.add(model);
                                    }
                                    else if (model.getOrderedDeliveryDate()==null) {
                                        receivedList.add(model);
                                    }
                                    else {
                                        deliveredList.add(model);
                                    }
                                    fetchImage(model);

                                    orderModels.add(model);
                                }
                            }
                            orderRcv.setVisibility(View.VISIBLE);

                            orderProgressBar.animate()
                                    .alpha(0f)
                                    .setDuration(getResources().getInteger(
                                            android.R.integer.config_shortAnimTime))
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            orderProgressBar.setVisibility(View.GONE);
                                            Log.d("size", "onComplete: "+orderedList.size());
                                            if (orderedList.size()==0) {
                                                ifOrderEmpty.setVisibility(View.VISIBLE);
                                                ifOrderEmpty.setText("No Orders Present");
                                            }
                                            else
                                                ifOrderEmpty.setVisibility(View.GONE);

                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });

                            orderRcv.setAdapter(adapter);
                        }
                        else{
                            Log.d("firebase", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    public void uploadDeliveryDate(OrderModel query){
        db.collection("agents")
                .document(sharedPrefUtils.readAgentId())
                .collection("orders")
                .document(query.getOrderedId())
                .set(query).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(deliveredBtn,"Data pushed to backend storage", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchImage(final OrderModel model){
        db.collection("products")
                .document(model.getOrderedProductId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        imageMap.put(task.getResult().getId(), (String) task.getResult().get("product_image"));
                        Log.d("orderImage", "onComplete: "+imageMap);
                        adapter.notifyDataSetChanged();
                    }
                });

    }
}
