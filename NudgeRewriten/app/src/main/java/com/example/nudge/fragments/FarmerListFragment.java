package com.example.nudge.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ComplexColorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.activities.AddFarmerActivity;
import com.example.nudge.adapters.Adapter_contact_list_1;
import com.example.nudge.models.FarmerModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FarmerListFragment extends Fragment {

    FloatingActionButton fabAdd;
    FirebaseFirestore db;
    SharedPrefUtils sharedPrefUtils;
    List<FarmerModel> farmers = new ArrayList<>();
    List<String> data = new ArrayList<>();
    Adapter_contact_list_1 adapter_contact_list_1;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return inflater.inflate(R.layout.fragment_farmerlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView farmer_contact_recycler = view.findViewById(R.id.farmer_contact_recycler);

        db=FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        farmer_contact_recycler.setNestedScrollingEnabled(false);
        fabAdd = view.findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddFarmerActivity.class));
            }
        });

        context = getActivity();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Fetch data from backend

        sharedPrefUtils = new SharedPrefUtils(getActivity());

        farmer_contact_recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter_contact_list_1 = new Adapter_contact_list_1(data,farmers,context);
        farmer_contact_recycler.setAdapter(adapter_contact_list_1);

        db.collection("agents/"+sharedPrefUtils.readAgentId()+"/farmers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()) {

                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d: list) {
                        FarmerModel farmer = d.toObject(FarmerModel.class);
                        farmers.add(farmer);
                        data.add(farmer.getName());
                    }

                    Collections.sort(farmers, new Comparator<FarmerModel>() {
                        @Override
                        public int compare(FarmerModel farmer1, FarmerModel farmer2) {
                            return farmerSort(farmer1, farmer2);
                        }
                    });

                    for(FarmerModel f: farmers) {
                        Log.i("Farmers are ",f.getName());
                    }

                    adapter_contact_list_1.notifyDataSetChanged();
                }
            }
        });


        // Search View Implementation.

        SearchView farmerlist_searchview = view.findViewById(R.id.farmerlist_searchview);

        farmerlist_searchview.clearFocus();

        farmerlist_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            List<String> filterOnChange = data;
            List<FarmerModel> filterFarmers = farmers;

            List<String> filterOnSubmit = new ArrayList<>();
            List<FarmerModel> farmerModels = new ArrayList<>();

            @Override
            public boolean onQueryTextSubmit(String s) {
                List<String> filterData = new ArrayList<>();
                int index=0;
                for (String filter : data
                ) {
                    if (s.length()<=filter.length()) {

                        if (s.equalsIgnoreCase(filter.substring(0, s.length()))) {
                            farmerModels.add(filterFarmers.get(index));
                            filterData.add(filter);
                        }
                    }
                    index++;
                    Log.i("search", "filter: " + filterData);
                }

                if (!filterData.isEmpty())
                    farmer_contact_recycler.setAdapter(new Adapter_contact_list_1(filterData,farmerModels,context));

                else {
                    Toast toast= Toast.makeText(getContext(), "No contacts found", Toast.LENGTH_SHORT);
                    toast.show();
                    farmer_contact_recycler.setAdapter(new Adapter_contact_list_1(data,farmers,context));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                List<String> filterData = new ArrayList<>();
                int index=0;
                    for (String filter : data
                    ) {
                        if (s.length()<=filter.length()) {

                            if (s.equalsIgnoreCase(filter.substring(0, s.length()))) {
                                farmerModels.add(farmers.get(index));
                                filterData.add(filter);
                            }
                            index++;
                        }
                        Log.i("search", "filter: " + filterData);
                    }

                    if (!filterData.isEmpty())
                        farmer_contact_recycler.setAdapter(new Adapter_contact_list_1(filterData,farmerModels,context));

                    else {

                        Toast toast=Toast.makeText(getContext(), "No contacts found", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK,0,0);
                        toast.show();
                        farmer_contact_recycler.setAdapter(new Adapter_contact_list_1(data,farmers,context));
                    }
                    return false;
            }

        });
    }

    public int farmerSort(FarmerModel f1,FarmerModel f2) {
        return f1.getName().compareTo(f2.getName());
    }
}