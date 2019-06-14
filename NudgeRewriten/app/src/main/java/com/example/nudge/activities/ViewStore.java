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
    public String productId;
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
        adapter = new ProductViewAdapter(Products,context);
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                if (documentSnapshot.exists()) {
                                    products products = documentSnapshot.toObject(com.example.nudge.models.products.class);
                                    Products.add(products);
                                }
                            }
                        }
                        else{
                            Log.d("firebase", "Error getting documents: ", task.getException());
                        }
                        prodRcvId.setAdapter(adapter);
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

        Integer position = getIntent().getIntExtra("POSITION",0);

        prodRcvId.scrollToPosition(position);
        adapter.click(position);
    }

    public void showPopUps(View v) {
        Intent intent = new Intent(context, SelectFarmerActivity.class);

        if (getIntent().getExtras().getString("fromActivity")!=null && getIntent().getExtras().getString("fromActivity").equals("FarmerProfileActivity")){
            intent.putExtra("farmerId", getIntent().getExtras().getString("farmerId"));
            intent.putExtra("farmerName",getIntent().getExtras().getString("farmerId"));
            intent.putExtra("fromActivity","FarmerProfileActivity");//to know from where ViewStore activity was called
        }
        else {
            intent.putExtra("fromActivity","ViewStoreActivity");//to know from where SelectFarmer activity was called
        }
        intent.putExtra("productId", productId);
        startActivity(intent);

    }

}
