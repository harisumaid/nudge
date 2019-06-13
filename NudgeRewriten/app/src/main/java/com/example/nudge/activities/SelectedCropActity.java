package com.example.nudge.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nudge.R;
import com.example.nudge.models.crop;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelectedCropActity extends AppCompatActivity {

    ImageView backBtn;

    FirebaseFirestore db;

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

        final ImageView imageView = findViewById(R.id.selectedCropImage);
        imageView.setImageResource(R.drawable.tomatoes);
        final TextView textView1 = findViewById(R.id.selectedCropName);
        final TextView cropDesc = findViewById(R.id.crop_desc);

        String id = getIntent().getStringExtra("ID");
        db = FirebaseFirestore.getInstance();

        db.collection("crops").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                crop cropModel = documentSnapshot.toObject(crop.class);
                textView1.setText(cropModel.getName());
                Glide.with(getApplicationContext()).load(cropModel.getImage()).into(imageView);
                cropDesc.setText(cropModel.getDesc());
            }
        });

    }
}
