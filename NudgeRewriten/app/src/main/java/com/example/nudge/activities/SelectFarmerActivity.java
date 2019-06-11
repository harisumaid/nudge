package com.example.nudge.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.adapters.Adapter_select_farmer_1;
import com.example.nudge.models.FarmerModel;
import com.example.nudge.models.OrderModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectFarmerActivity extends AppCompatActivity {

    public boolean flag[];
    public List<String> select_farmer;

    public List<OrderModel> orderPlaced = new ArrayList<>();
    List<FarmerModel> farmers = new ArrayList<>();
    public String productName;
    String productReceivingDate;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    SharedPrefUtils sharedPrefUtils;
    Adapter_select_farmer_1 adapter;

    String id;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_farmer);
        context = this;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        sharedPrefUtils = new SharedPrefUtils(context);

        //  place order button
        Button select_farmer_order_button;
        select_farmer_order_button = findViewById(R.id.select_farmer_order_button);

        select_farmer=new ArrayList<>();//supplying the name of farmer after fetching

        final RecyclerView dialogRecycler =findViewById(R.id.dialog_recycler);//Main recycler view

        Button select_farmer_cancel_button = findViewById(R.id.select_farmer_cancel_button);//Cancel Button
        select_farmer_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //        Fetching farmers list from database
        db.collection("agents").document(sharedPrefUtils.readAgentId()).collection("farmers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                if (documentSnapshot.exists()) {
        // here fetching of data from collection of farmer is done where name and image of each farmer is fetched
                                    Log.d("firebase", "onComplete: " + select_farmer);
                                    farmers.add(documentSnapshot.toObject(FarmerModel.class));
                                    select_farmer.add((String) documentSnapshot.get("name"));
                                }
                            }
                        }
                        else{
                            Log.d("firebase", "Error getting documents: ", task.getException());
                        }

                        Collections.sort(select_farmer);
                        Log.d("firebase", "onComplete: "+select_farmer);
                        List<FarmerModel> sortedFarmers = new ArrayList<>();

                        for(String name: select_farmer) {
                            for(FarmerModel f: farmers) {
                                if(name.compareTo(f.getName())==0)
                                    sortedFarmers.add(f);
                            }
                        }

                        farmers = sortedFarmers;

                        Toast.makeText(context, "starting to fetch", Toast.LENGTH_SHORT).show();

        // Calling the adapter that would show the list of farmers to select from
                        dialogRecycler.setLayoutManager(new LinearLayoutManager(context));
                        dialogRecycler.setHasFixedSize(true);
                        adapter = new Adapter_select_farmer_1(select_farmer,farmers,context);
                        dialogRecycler.setAdapter(adapter);
                        flag = new boolean[farmers.size()];
                    }

                });

        SearchView select_farmer_searchview = findViewById(R.id.select_farmer_searchview);

        select_farmer_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            List<FarmerModel> selectedFarmers = new ArrayList<>();

            List<String> filterOnSubmit = new ArrayList<>();
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<String> filterData = new ArrayList<>();
                int index = 0;
                for (String filter : select_farmer) {
                    if (s.length()<=filter.length()) {
                        if (s.equalsIgnoreCase(filter.substring(0, s.length()))) {
                            filterData.add(filter);
                            selectedFarmers.add(farmers.get(index));
                        }
                    }
                    index++;
                    Log.i("search", "filter: " + filterData);
                }

                if (!filterData.isEmpty())
                    dialogRecycler.setAdapter(new Adapter_select_farmer_1(filterData,selectedFarmers,context));

                else {
                    Toast toast=Toast.makeText(context, "No contacts found", Toast.LENGTH_SHORT);
                    toast.show();
                    dialogRecycler.setAdapter(new Adapter_select_farmer_1(select_farmer,farmers,context));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                List<String> filterData = new ArrayList<>();
                List<FarmerModel> farmerModels = new ArrayList<>();
                int index=0;
                for (String filter : select_farmer
                ) {
                    if (s.length()<=filter.length()) {

                        if (s.equalsIgnoreCase(filter.substring(0, s.length()))) {
                            filterData.add(filter);
                            farmerModels.add(farmers.get(index));
                        }
                    }
                    index++;
                    Log.i("search", "filter: " + filterData);
                }

                if (!filterData.isEmpty())
                    dialogRecycler.setAdapter(new Adapter_select_farmer_1(filterData,farmerModels,context));

                else {

                    Toast toast=Toast.makeText(context, "No contacts found", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK,0,0);
                    toast.show();
                    dialogRecycler.setAdapter(new Adapter_select_farmer_1(select_farmer,farmers,context));
                }
                return false;
            }

        });

        //    placing the order by passing values to the OrderActivity
        // Storing the contacts that we need to send if that item was selected
        select_farmer_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                List<String> names = adapter.getNames();

                Intent returnIntent = new Intent();

                returnIntent.putStringArrayListExtra("result", (ArrayList<String>) names);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();


//                List<String> sendOrder=new ArrayList<>();
//
//                for (int i=0;i<farmers.size();i++) {
//                    if (flag[i]){
//                        sendOrder.add(farmers.get(i).getName());
//                        Log.d("order", "onClick: "+sendOrder);
//                    }
//                }
//
//                Log.d("Selected Farmers are :",sendOrder.toString());
//
//                Toast.makeText(context, "Order Placed", Toast.LENGTH_SHORT).show();
        //                ViewStore viewStore;
        //                viewStore = (ViewStore) getParent();
        //                viewStore.selected_farmer=sendOrder;
        //                viewStore.placeOrder();

//                productName = getIntent().getExtras().getString("productName");
//                productReceivingDate = getIntent().getExtras().getString("product_receiving_date");
//                placeOrder(sendOrder);
//                finish();
            }
        });
    }

    public void placeOrder(List<String> sendOrder){
        for (String individualFarmer:sendOrder) {
    //            HAVE TO ADD orderPlaced LIST TO SHARED PREFERENCE FOR REFLECTING IN ORDERS ACTIVITY
            orderPlaced.add(new OrderModel(individualFarmer,productName,productReceivingDate));
        }
        savedSharedpref(orderPlaced);

    }
    public  void savedSharedpref(List<OrderModel> orderPlaced)
    {
        ((OrderPlaced) this.getApplication()).addOrderPlaced(orderPlaced);
        preferences = getSharedPreferences("MyPref",MODE_PRIVATE);
        editor= preferences.edit();
        Gson gson= new Gson();
        String json = gson.toJson(((OrderPlaced) this.getApplication()).getOrderPlaced());
        editor.putString("OrderPlaced", json);
        Log.i("TAG", "placeOrder: SharedPrefs Saved ");
        editor.commit();
    }


}