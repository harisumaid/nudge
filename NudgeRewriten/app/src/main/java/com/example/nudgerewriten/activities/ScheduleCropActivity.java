package com.example.nudgerewriten.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nudgerewriten.R;
import com.example.nudgerewriten.adapters.ScheduleCropAdapter;

public class ScheduleCropActivity extends AppCompatActivity {

    ImageView backBtn;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ScheduleCropAdapter adapter;

    int count = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_crop);

        recyclerView = findViewById(R.id.scheduled_crop_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ScheduleCropAdapter(count);
        recyclerView.setAdapter(adapter);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
       //
        /**/


    }
    public void addCropdetail(View view)
    {

        count++;
        adapter.updateCount(count);
    }

}
