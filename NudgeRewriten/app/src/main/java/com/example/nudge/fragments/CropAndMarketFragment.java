package com.example.nudge.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.adapters.RecyclerAdapter;
import com.example.nudge.models.crop;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CropAndMarketFragment extends Fragment {

    private RecyclerView recyclerView;

    private ArrayList<String> croplist=new ArrayList<>();

    private RecyclerView.LayoutManager layoutManager;

    private RecyclerAdapter adapter;
    private SearchView crop_market_searchview;
    private ArrayList<crop> cropsList = new ArrayList<>();
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crop_and_market, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerview);

        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(cropsList,getActivity());
        recyclerView.setAdapter(adapter);
        crop_market_searchview = view.findViewById(R.id.crop_market_searchview);

        db.collection("crops").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         if(!queryDocumentSnapshots.isEmpty()) {

                             List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                             for(DocumentSnapshot d: list) {
                                 crop cropModel  = d.toObject(crop.class);
                                 cropsList.add(cropModel);
                                 croplist.add(cropModel.getName());
                             }
                             adapter.notifyDataSetChanged();
                         }
            }
        });

        crop_market_searchview.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            List<crop> filterOnSubmit = new ArrayList<>();
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<String> filterData = new ArrayList<>();
                for (String filter : croplist) {
                    if (s.length()<=filter.length()) {

                        if (s.equalsIgnoreCase(filter.substring(0, s.length()))) {
                            filterData.add(filter);
                        }
                    }
                    Log.i("search", "filter: " + filterData);
                }

                for(String name: filterData) {
                    for(crop c: cropsList)
                        if(name.compareTo(c.getName())==0)
                            filterOnSubmit.add(c);
                }

                if (!filterData.isEmpty()){

                    adapter = new RecyclerAdapter(filterOnSubmit,getContext());
                    recyclerView.setAdapter(adapter);


                }
                else {
                    Toast toast=Toast.makeText(getContext(), "No Crops found", Toast.LENGTH_SHORT);
                    toast.show();
                    adapter = new RecyclerAdapter(cropsList,getContext());
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<String> filterData = new ArrayList<>();
                for (String filter : croplist) {
                    if (s.length()<=filter.length()) {
                        if (s.equalsIgnoreCase(filter.substring(0, s.length()))) {
                            if (s.equalsIgnoreCase(filter.substring(0, s.length()))) {
                                filterData.add(filter);
                            }
                        }
                    }
                    Log.i("search", "filter: " + filterData);
                }

                filterOnSubmit.clear();
                for(String name: filterData) {
                    for(crop c: cropsList)
                        if(name.compareTo(c.getName())==0)
                        {  filterOnSubmit.add(c);
                            break; }
                }


                if (!filterData.isEmpty()){
                    adapter = new RecyclerAdapter(filterOnSubmit,getContext());
                    recyclerView.setAdapter(adapter);
                }
                else {
                    Toast toast=Toast.makeText(getContext(), "No crops found", Toast.LENGTH_SHORT);
                    toast.show();
                    adapter = new RecyclerAdapter(cropsList,getContext());
                    recyclerView.setAdapter(adapter);
                }
                return false;

            }

        });




    }
}