package com.example.nudge.activities;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nudge.R;
import com.example.nudge.adapters.ScheduledCropsAdapter;
import com.example.nudge.fragments.FarmerListFragment;
import com.example.nudge.models.CropModel;
import com.example.nudge.models.FarmerModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rhexgomez.typer.roboto.TyperRoboto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FarmerProfileActivity extends AppCompatActivity {

    ImageView backBtn,farmerImg;
    Toolbar toolbar;
    TextView scheduleBtn,primaryNo,secondaryNo,farmerPlace,storeBtn;
    RecyclerView scheduledCropRcv;
    ScheduledCropsAdapter adapter;
    ProgressBar farmerPb;

    List<CropModel> crops = new ArrayList<>();
    SharedPrefUtils sharedPrefUtils;

    FirebaseFirestore db;

    private static CollapsingToolbarLayout collapsingToolbarLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        collapsingToolbarLayout2 = findViewById(R.id.collapsing_toolbar2);

        collapsingToolbarLayout2.setCollapsedTitleTypeface(TyperRoboto.ROBOTO_REGULAR());
        collapsingToolbarLayout2.setExpandedTitleTypeface(TyperRoboto.ROBOTO_REGULAR());
        farmerImg = findViewById(R.id.farmer_photo);
        db = FirebaseFirestore.getInstance();

        farmerPb = findViewById(R.id.profile_pb);

        storeBtn = findViewById(R.id.store_btn);

        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ViewStore.class));
            }
        });

        sharedPrefUtils = new SharedPrefUtils(this);

        final FarmerModel farmerInfo = getIntent().getParcelableExtra("FarmerInfo");

        if(farmerInfo!=null) {

            collapsingToolbarLayout2.setTitle(farmerInfo.getName());

            Glide.with(this).load(farmerInfo.getImage()).into(farmerImg);

            backBtn = findViewById(R.id.back_btn);
            toolbar = findViewById(R.id.toolbar_farmer_add);
            primaryNo = findViewById(R.id.primary_no);
            secondaryNo = findViewById(R.id.secondary_no);
            farmerPlace = findViewById(R.id.farmer_place);

            primaryNo.setText("+91 "+farmerInfo.getPrimary_contact_number());
            secondaryNo.setText("+91 "+farmerInfo.getSecondary_contact_number());
            farmerPlace.setText(farmerInfo.getAddress());

            CollectionReference scheduledCrops = db.collection("agents/"+sharedPrefUtils.readAgentId()+"/farmers/"+farmerInfo.getId()+"/crops");

            scheduledCrops.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for(DocumentSnapshot d: list) {
                            CropModel crop = d.toObject(CropModel.class);
                            crops.add(crop);
                            adapter.notifyDataSetChanged();
                        }
                        farmerPb.setVisibility(View.INVISIBLE);
                    }
                }
            });

            scheduleBtn = findViewById(R.id.schedule_btn);
            scheduledCropRcv = findViewById(R.id.scheduled_crop_rcv);

            scheduledCropRcv.setLayoutManager(new GridLayoutManager(this,2));
            adapter = new ScheduledCropsAdapter(crops,this,farmerInfo.getId());
            scheduledCropRcv.setAdapter(adapter);

            scheduleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(),ScheduleCropActivity.class);
                    intent.putExtra("FARMERID",farmerInfo.getId());

                    startActivity(intent);
                }
            });

            toolbar.inflateMenu(R.menu.actions);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getItemId() == R.id.actions_add) {
                        Intent intent = new Intent(getApplicationContext(),AddFarmerActivity.class);
                        intent.putExtra("FarmerId",farmerInfo.getId());
                        startActivity(intent);
                        Toast.makeText(FarmerProfileActivity.this, "You are about to edit the contact.", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if(menuItem.getItemId() == R.id.actions_delete) {

                        Toast.makeText(FarmerProfileActivity.this, "You are about to delete the contact.", Toast.LENGTH_SHORT).show();

                        db.collection("agents").document(sharedPrefUtils.readAgentId()).collection("farmers").document(farmerInfo.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FarmerProfileActivity.this, "Farmer Successully Removed.", Toast.LENGTH_SHORT).show();
                                View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);
                                View v = inflatedView.findViewById(R.id.fragment_container);

                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("ID","delete");
                                startActivity(intent);
                            }
                        });

                        return true;
                    } else {
                        return false;
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }
}
