package com.example.nudge.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.nudge.R;
import com.example.nudge.adapters.VisitsAdapter;
import com.example.nudge.models.VisitModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VisitsActivity extends AppCompatActivity {

    ImageView backBtn;
    RecyclerView todaysRcv,pendingRcv,upcomingRcv;
    VisitsAdapter adapter1,adapter2,adapter3;
    List<VisitModel> pendingVisits = new ArrayList<>();
    List<VisitModel> todayVisits = new ArrayList<>();
    List<VisitModel> upcomingVisits = new ArrayList<>();

    ScrollView visitView;
    ProgressBar visitProgressBar;

    CardView pending_sample_text,today_sample_text,upcoming_sample_text;

    FirebaseFirestore db;
    SharedPrefUtils sharedPrefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);
        Toolbar toolbar = findViewById(R.id.toolbar_farmer_add);
//        setSupportActionBar(toolbar);

        backBtn = findViewById(R.id.back_btn1);
        todaysRcv = findViewById(R.id.todays_rcv);
        pendingRcv = findViewById(R.id.pending_rcv);
        upcomingRcv = findViewById(R.id.upcoming_rcv);

        pending_sample_text = findViewById(R.id.pending_default_card);
        today_sample_text = findViewById(R.id.today_default_card);
        upcoming_sample_text = findViewById(R.id.upcoming_default_card);

        visitView = findViewById(R.id.visit_default);
        visitView.setVisibility(View.INVISIBLE);
        visitProgressBar = findViewById(R.id.progress_bar_visit);
        visitProgressBar.setVisibility(View.VISIBLE);

        final long shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        db = FirebaseFirestore.getInstance();
        sharedPrefUtils = new SharedPrefUtils(this);

        db.collection("agents").document(sharedPrefUtils.readAgentId()).collection("visits").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()!=0) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d: list) {
                        VisitModel visit = d.toObject(VisitModel.class);

                        if(visit.getVisit_status()) continue;

                        if(visit.getVisitDate().after(new Date())) {
                            upcomingVisits.add(visit);
                        } else if(visit.getVisitDate().before(new Date())) {
                            pendingVisits.add(visit);
                        } else {
                            todayVisits.add(visit);
                        }

                    }

                    if(upcomingVisits.isEmpty()) upcoming_sample_text.setVisibility(View.VISIBLE); else {
                        ViewGroup parent = (ViewGroup) upcoming_sample_text.getParent();
                        if (parent != null) {
                            parent.removeView(upcoming_sample_text);
                        }
                    }
                    if(pendingVisits.isEmpty()) pending_sample_text.setVisibility(View.VISIBLE); else {
                        ViewGroup parent = (ViewGroup) pending_sample_text.getParent();
                        if (parent != null) {
                            parent.removeView(pending_sample_text);
                        }
                    }

                    if(todayVisits.isEmpty()) today_sample_text.setVisibility(View.VISIBLE); else {
                        ViewGroup parent = (ViewGroup) today_sample_text.getParent();
                        if (parent != null) {
                            parent.removeView(today_sample_text);
                        }
                    }

                    adapter1.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                    adapter3.notifyDataSetChanged();
                }

                visitView.animate()
                        .alpha(1f)
                        .setDuration(shortAnimationDuration)
                        .setListener(null);

                visitView.setVisibility(View.VISIBLE);

                visitProgressBar.animate()
                        .alpha(0f)
                        .setDuration(shortAnimationDuration)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                visitProgressBar.setVisibility(View.GONE);
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

        todaysRcv.setLayoutManager(new LinearLayoutManager(this));
        todaysRcv.setItemAnimator(new DefaultItemAnimator());
        todaysRcv.setNestedScrollingEnabled(false);

        adapter1 = new VisitsAdapter(todayVisits,this);
        todaysRcv.setAdapter(adapter1);

        adapter2 = new VisitsAdapter(pendingVisits,this);
        pendingRcv.setLayoutManager(new LinearLayoutManager(this));
        pendingRcv.setItemAnimator(new DefaultItemAnimator());
        pendingRcv.setNestedScrollingEnabled(false);
        pendingRcv.setAdapter(adapter2);

        adapter3 = new VisitsAdapter(upcomingVisits,this);
        upcomingRcv.setLayoutManager(new LinearLayoutManager(this));
        upcomingRcv.setItemAnimator(new DefaultItemAnimator());
        upcomingRcv.setNestedScrollingEnabled(false);
        upcomingRcv.setAdapter(adapter3);

        FloatingActionButton fab = findViewById(R.id.visits_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NewVisitActivity.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
