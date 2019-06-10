package com.example.nudge.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nudge.R;
import com.example.nudge.adapters.ScheduleCropAdapter;

public class ScheduleCropActivity extends AppCompatActivity {

    ImageView backBtn;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ScheduleCropAdapter adapter;
    Button saveBtn;
    ProgressBar cropProgressBar;

    int count = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_crop);

        recyclerView = findViewById(R.id.scheduled_crop_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ScheduleCropAdapter(getIntent().getStringExtra("FARMERID"),this);
        recyclerView.setAdapter(adapter);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cropProgressBar = findViewById(R.id.crop_progress_bar);

        saveBtn = findViewById(R.id.save_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropProgressBar.setVisibility(View.VISIBLE);
                adapter.uploadCropDetails();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public void addCropdetail(View view) {
        count++;
        adapter.updateCount(count);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
