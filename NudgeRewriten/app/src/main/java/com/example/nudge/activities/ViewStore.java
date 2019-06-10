package com.example.nudge.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.adapters.ProductViewAdapter;
import com.example.nudge.models.products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewStore extends AppCompatActivity {


    public RecyclerView prodRcvId;
    public RecyclerView.LayoutManager layoutManager;
    public List<String> selected_farmer= new ArrayList<>();

    //    Products (LIST) variable is the variable that is used for keeping the product details
    List<products> Products = new ArrayList<>();

    //    variables about placing the orders
    public String productName;
    String orderReceivingDate;

    private Toolbar toolbar;
    ImageView viewstore_backbtn;
    private ProductViewAdapter adapter;
    ConstraintLayout viewstore_base_layout;
    ProgressBar progress_bar_viewstore;
    Context context;

//    Dialog selectFarmer;

    FirebaseFirestore db;
    ArrayList<products> selectionlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store);
        toolbar= (Toolbar) findViewById(R.id.viewstore_toolbar);
        progress_bar_viewstore=findViewById(R.id.progress_bar_viewstore);
        viewstore_base_layout = findViewById(R.id.viewstore_base_layout);
        context = this;
        viewstore_backbtn=findViewById(R.id.viewstore_backbtn);
//        selectFarmer=new Dialog(this);

// setting onclick listener to work as back button.
        viewstore_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

// making the viewstore_base_layout invisible while its loading and fading it in when the fetching of data is complete
        viewstore_base_layout.setVisibility(View.VISIBLE);
        final long shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                if (documentSnapshot.exists()) {
                                    Products.add(new products((String)documentSnapshot.get("product_name"),(String)documentSnapshot.get("product_company"),((Long)documentSnapshot.get("product_price")).intValue() ,R.drawable.fertilizer));
                                }
                            }
                        }
                        else{
                            Log.d("firebase", "Error getting documents: ", task.getException());
                        }
                        adapter = new ProductViewAdapter(Products,context);
                        prodRcvId.setAdapter(adapter);
                        Toast.makeText(ViewStore.this, "fetched", Toast.LENGTH_SHORT).show();
                        viewstore_base_layout.animate()
                                .alpha(1f)
                                .setDuration(shortAnimationDuration)
                                .setListener(null);

                        progress_bar_viewstore.animate()
                                .alpha(0f)
                                .setDuration(shortAnimationDuration)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        progress_bar_viewstore.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                    }
                });

        selectionlist = new ArrayList<>();
        prodRcvId=findViewById(R.id.recyclerViewStore);
        layoutManager = new LinearLayoutManager(this);
        prodRcvId.setHasFixedSize(true);
        prodRcvId.setLayoutManager(layoutManager);
    }

    public void showPopUps(View v) {
        Intent intent = new Intent(context, SelectFarmerActivity.class);
        intent.putExtra("productName", productName);
        intent.putExtra("product_receiving_date",receivingDate(productName));
        startActivity(intent);

    }

    public String receivingDate(String productId){
        final String[] orderReceivingDate = {""};
        //       fetching the receiving date value by the productId supplied from placeOrder() method.

        db.collection("products").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    orderReceivingDate[0] = (String) document.get("receiving_date");
                    Log.d("firebase", "onReceivingDate "+ orderReceivingDate[0]);
                } else {
                    Log.d("error", "Cached get failed: ", task.getException());
                }

            }
        });

        return orderReceivingDate[0];
    }




//        THIS PART HAS BEEN COMENTED OUT DUE TO FAILURE WHILE SHOWING SELECT_FARMER AS DIALOG
//        MAY BE DONE LATER

//        select_farmer=new ArrayList<>();//supplying the name of farmer after fetching
//
//        selectFarmer.setContentView(R.layout.activity_select_farmer);
//
//        final RecyclerView dialogRecycler = selectFarmer.findViewById(R.id.dialog_recycler);//Main recycler view
//
//        final Button select_farmer_cancel_button = selectFarmer.findViewById(R.id.select_farmer_cancel_button);//Cancel Button
//        select_farmer_cancel_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectFarmer.dismiss();
//            }
//        });
//
//
////        Fetching farmers list from database
//        db.collection("agents").document("nudge@123").collection("farmers")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                                if (documentSnapshot.exists()) {
//// here fetching of data from collection of farmer is done where name and image of each farmer is fetched
//                                    Log.d("firebase", "onComplete: " + select_farmer);
//                                    select_farmer.add((String) documentSnapshot.get("name"));
//
//                                }
//                            }
//                        }
//                        else{
//                            Log.d("firebase", "Error getting documents: ", task.getException());
//                        }
//                        Collections.sort(select_farmer);
//                        Log.d("firebase", "onComplete: "+select_farmer);
//                        Toast.makeText(context, "starting to fetch", Toast.LENGTH_SHORT).show();
//
//// Calling the adapter that would show the list of farmers to select from
//                        dialogRecycler.setLayoutManager(new LinearLayoutManager(context));
//                        dialogRecycler.setHasFixedSize(true);
//                        dialogRecycler.setAdapter(new Adapter_select_farmer_1(select_farmer));
//                    }
//
//                });
//
//        selectFarmer.show();


}
