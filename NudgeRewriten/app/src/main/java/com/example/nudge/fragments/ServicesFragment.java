package com.example.nudge.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.activities.ViewStore;

public class ServicesFragment extends Fragment {
    TextView textView,textView1,textView2,textView3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_services ,container, false);
        setHasOptionsMenu(true);
        return  v;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        textView = getView().findViewById(R.id.buyproducts);
        textView1=getView().findViewById(R.id.loan);
        textView2=getView().findViewById(R.id.insurance);
        textView3=getView().findViewById(R.id.testing);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewStore.class));
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Not Available in Your Area", Toast.LENGTH_SHORT).show();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Not Available in Your Area", Toast.LENGTH_SHORT).show();
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Not Available in Your Area", Toast.LENGTH_SHORT).show();
            }
        });
    }

}