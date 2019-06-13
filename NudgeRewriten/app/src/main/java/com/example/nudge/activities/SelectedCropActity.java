package com.example.nudge.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nudge.R;

public class SelectedCropActity extends AppCompatActivity {

    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_crop_actity);

//        Intent intent=getIntent();
//        crop crop=intent.getParcelableExtra("Crop Name");

//        int cropimage = crop.getCropimage();
//        String cropname = crop.getCropname();

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView imageView = findViewById(R.id.selectedCropImage);
        imageView.setImageResource(R.drawable.tomatoes);
        TextView textView1 = findViewById(R.id.selectedCropName);
        textView1.setText("Tomatoes");
    }
}
