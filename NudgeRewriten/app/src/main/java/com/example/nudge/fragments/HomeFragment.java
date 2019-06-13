package com.example.nudge.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.activities.OrdersActivity;
import com.example.nudge.activities.ViewStore;
import com.example.nudge.activities.VisitsActivity;
import com.example.nudge.adapters.ProductAdapter;
import com.example.nudge.models.products;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class HomeFragment extends Fragment {

    RecyclerView prodRcvId;
    TextView visitId,orderId,viewAll;
    List<products> Products = new ArrayList<>();
    Toolbar toolbar;
    ProgressBar progressBar_home;

    ImageView visitImg,orderImg;

    ProductAdapter adapter;

    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prodRcvId = getView().findViewById(R.id.prod_rcv_id);
        orderId = getView().findViewById(R.id.order_id);
        visitId = getView().findViewById(R.id.visit_id);

        visitImg = getView().findViewById(R.id.visit_image);
        orderImg = getView().findViewById(R.id.order_image);

        viewAll = getView().findViewById(R.id.view_title);

        progressBar_home = getView().findViewById(R.id.progressBar_home);

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewStore.class);
                intent.putExtra("fromActivity","HomeFragment");
                startActivity(intent);
            }
        });

        Bitmap order_pic = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.cucumbers);

        Bitmap visit_pic = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.cropland);

        Blurry.with(getActivity()).radius(10);
        Blurry.with(getActivity()).sampling(8);
        Blurry.with(getActivity()).async();

        Blurry.with(getActivity()).color(Color.argb(1, 255, 255, 0)).from(order_pic).into(orderImg);
        Blurry.with(getActivity()).from(visit_pic).into(visitImg);

        toolbar = getView().findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.home_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.notifs)
                {   Toast.makeText(getActivity(), "You have 0 notifications.", Toast.LENGTH_SHORT).show();
                    return true; }
                else if(item.getItemId() == R.id.cart) {
                    Toast.makeText(getActivity(), "You have 0 items in cart", Toast.LENGTH_SHORT).show();
                    return true;
                } else return false;
            }
        });

        prodRcvId.setLayoutManager(new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false));
        adapter = new ProductAdapter(Products,getActivity());
        prodRcvId.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        db.collection("products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d: list) {
                        products product = d.toObject(products.class);
                        Products.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar_home.setVisibility(View.INVISIBLE);
            }
        });

        orderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrdersActivity.class));
            }
        });

        visitImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VisitsActivity.class));
            }
        });

    }
}