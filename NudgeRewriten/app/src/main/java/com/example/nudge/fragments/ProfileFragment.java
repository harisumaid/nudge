package com.example.nudge.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.models.AgentModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rhexgomez.typer.roboto.TyperRoboto;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private static CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView agentImg;
    ProgressBar progress;
    TextView farmerPts,bronze,silver,gold;

    SharedPreferences agentId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        agentId=getActivity().getSharedPreferences("com.example.nudge", Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar2);

        collapsingToolbarLayout.setCollapsedTitleTypeface(TyperRoboto.ROBOTO_REGULAR());
        collapsingToolbarLayout.setExpandedTitleTypeface(TyperRoboto.ROBOTO_REGULAR());

        String id = agentId.getString("agentId","");
        updateAgentValues(id);

        String agentName = agentId.getString("agentName","");
        Integer agentPts = agentId.getInt("agentPts",0);
        final String agentLevel = agentId.getString("agentLevel","");
        final String agentImage = agentId.getString("agentImg","");

        farmerPts = view.findViewById(R.id.farmer_pts);
        farmerPts.setText(Integer.toString(agentPts)+" pts");

        agentImg = view.findViewById(R.id.agent_img);
        progress = view.findViewById(R.id.progress);

        progress.setScaleY(3f);

        progress.setProgress(agentPts);

        bronze = view.findViewById(R.id.bronze);
        silver = view.findViewById(R.id.silver);
        gold = view.findViewById(R.id.gold);

        bronze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "You are "+agentLevel.toLowerCase()+" Rated.", Toast.LENGTH_SHORT).show();
            }
        });

        silver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "You are "+agentLevel.toLowerCase()+" Rated.", Toast.LENGTH_SHORT).show();
            }
        });

        gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "You are "+agentLevel.toLowerCase()+" Rated.", Toast.LENGTH_SHORT).show();
            }
        });



        if((agentLevel.toLowerCase()).compareTo("bronze")==0) {
            bronze.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.member_circle));
            bronze.setTextColor(Color.WHITE);
        } else if((agentLevel.toLowerCase()).compareTo("silver")==0) {
            silver.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.member_circle));
            silver.setTextColor(Color.WHITE);
        } else {
            gold.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.member_circle));
            gold.setTextColor(Color.WHITE);
        }

        Picasso.get().load(agentImage).into(agentImg);
        collapsingToolbarLayout.setTitle(agentName);
    }

    public void updateAgentValues(String id) {

        db.collection("agents")
                .document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AgentModel agent = documentSnapshot.toObject(AgentModel.class);

                agentId.edit().putString("agentName",agent.getName()).apply();
                agentId.edit().putString("agentImg",agent.getImage()).apply();
                agentId.edit().putString("agentFarmersCnt",agent.getFarmers_count()).apply();
                agentId.edit().putString("agentLevel",agent.getLevel()).apply();
                agentId.edit().putInt("agentPts",agent.getPoints()).apply();
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}