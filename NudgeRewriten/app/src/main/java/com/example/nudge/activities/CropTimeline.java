package com.example.nudge.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nudge.R;
import com.example.nudge.models.CropModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.qap.ctimelineview.TimelineRow;
import org.qap.ctimelineview.TimelineViewAdapter;

import java.util.ArrayList;

public class CropTimeline extends AppCompatActivity {

    ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();
    Toolbar toolbar;
    ImageView backBtn,cropImage;
    TextView cropName;

    FirebaseFirestore db;
    SharedPrefUtils sharedPrefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_timeline);

        toolbar = findViewById(R.id.toolbar_farmer_add);
        backBtn = findViewById(R.id.back_btn1);

        cropImage = findViewById(R.id.crop_image_id);
        cropName = findViewById(R.id.crop_name_id);
        db = FirebaseFirestore.getInstance();

        sharedPrefUtils = new SharedPrefUtils(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String cropId = getIntent().getStringExtra("CROPID");
        String farmerId = getIntent().getStringExtra("FARMERID");

        // Create the Timeline Adapter
        final ArrayAdapter<TimelineRow> myAdapter = new TimelineViewAdapter(this, 0, timelineRowsList,
                //if true, list will be sorted by date
                false);

// Get the ListView and Bind it with the Timeline Adapter
        ListView myListView = (ListView) findViewById(R.id.timeline_listView);
        myListView.setAdapter(myAdapter);

        db.collection("agents").document(sharedPrefUtils.readAgentId()).collection("farmers").document(farmerId).collection("crops").document(cropId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              CropModel crop = documentSnapshot.toObject(CropModel.class);
              cropName.setText(crop.getCropname());
                Picasso.get().load(crop.getCropImage()).into(cropImage);


                TimelineRow myRow = new TimelineRow(0);

                myRow.setTitle("Day 1");
                myRow.setDescription(crop.getSeedingDate()+"\n" +
                        "\n"+"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
                myRow.setImage(getBitmap(R.drawable.timeline_circle));
                myRow.setBellowLineColor(Color.parseColor("#3CCC00"));
                myRow.setBellowLineSize(1);
                myRow.setImageSize(40);
                timelineRowsList.add(myRow);

                myRow = new TimelineRow(0);

                myRow.setTitle("Day 5");
                myRow.setDescription("Friday 8 May, 2019\n" +
                        "\n"+"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
                myRow.setImage(getBitmap(R.drawable.timeline_circle));
                myRow.setBellowLineColor(Color.parseColor("#3CCC00"));
                myRow.setBellowLineSize(1);
                myRow.setImageSize(40);
                timelineRowsList.add(myRow);

                myRow = new TimelineRow(0);

                myRow.setTitle("Day 7");
                myRow.setDescription("Sun 10 May, 2019\n" +
                        "\n"+"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
                myRow.setImage(getBitmap(R.drawable.timeline_circle));
                myRow.setBellowLineColor(Color.parseColor("#3CCC00"));
                myRow.setBellowLineSize(1);
                myRow.setImageSize(40);
                timelineRowsList.add(myRow);

                myRow = new TimelineRow(0);

                myRow.setTitle("Day 10");
                myRow.setDescription("Wed 13 April, 2019\n" +
                        "\n"+"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
                myRow.setImage(getBitmap(R.drawable.timeline_circle));
                myRow.setBellowLineColor(Color.parseColor("#3CCC00"));
                myRow.setBellowLineSize(1);
                myRow.setImageSize(40);
                timelineRowsList.add(myRow);

                myAdapter.notifyDataSetChanged();
            }

        });

    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
