package com.example.nudge.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.fragments.DatePickerFragment;
import com.example.nudge.fragments.TimePickerFragment;
import com.example.nudge.models.VisitModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewVisitActivity extends AppCompatActivity {

    ImageView crossBtn;
    EditText visitTitle;
    TextView contactSearchBtn,dateBtn,timeBtn;
    Button saveBtn;
    Date visit_date;
    Switch aSwitch;
    String time;
    List<String> ids = new ArrayList<>();
    SharedPrefUtils sharedPrefUtils;

    FirebaseFirestore db;

    static final int PICK_FARMER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visit);

        sharedPrefUtils = new SharedPrefUtils(this);

        crossBtn = findViewById(R.id.cross_btn);
        visitTitle = findViewById(R.id.visit_title);
        contactSearchBtn = findViewById(R.id.contact_search_btn);
        saveBtn = findViewById(R.id.save_btn);
        dateBtn = findViewById(R.id.date_btn);
        timeBtn = findViewById(R.id.time_btn);
        aSwitch = findViewById(R.id.switch1);

        db = FirebaseFirestore.getInstance();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) { timeBtn.setVisibility(View.INVISIBLE); time = "ALL day"; }
                else { timeBtn.setVisibility(View.VISIBLE); }
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datepicker");
            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timepicker");
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = visitTitle.getText().toString();
                if(visit_date==null) {
                    Toast.makeText(NewVisitActivity.this, "Please select a date.", Toast.LENGTH_SHORT).show();
                } else if(title== "") {
                    Toast.makeText(NewVisitActivity.this, "Please select a title.", Toast.LENGTH_SHORT).show();
                } else if(ids.size()==0) {
                    Toast.makeText(NewVisitActivity.this, "Please select a Farmer", Toast.LENGTH_SHORT).show();
                } else if(time=="") {
                    Toast.makeText(NewVisitActivity.this, "Select a time.", Toast.LENGTH_SHORT).show();
                } else {

                    CollectionReference visitRef = db.collection("agents").document(sharedPrefUtils.readAgentId()).collection("visits");

                    for(String id: ids) {
                        VisitModel visit = new VisitModel(id,title,time,"",false,visit_date);
                        visitRef.add(visit).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(NewVisitActivity.this, "Adding visits. Pls Wait.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Toast.makeText(NewVisitActivity.this, "Visits Details added.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

        contactSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactSearchBtn.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                Intent pickFarmerIntent = new Intent(getApplicationContext(),SelectFarmerActivity.class);
                startActivityForResult(pickFarmerIntent, PICK_FARMER_REQUEST);

//                Intent intent = new Intent(getApplicationContext(),SelectFarmerActivity.class);
//                intent.putExtra("PARENT","Visit");
//                startActivity(intent);
            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_FARMER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                ids=data.getStringArrayListExtra("result");
            }
        }
    }

    public void processDatePickerResult(int year, int month, int day, String date, Date visitDate) {
        dateBtn.setText(date);
        visit_date = visitDate;
    }

    public void processTimePickerResult(int hourOfDay, int minute) {
        // Convert time elements into strings.

        String hour_string = Integer.toString(hourOfDay);
        String minute_string = Integer.toString(minute);
        if(hourOfDay<10) hour_string = "0"+hour_string;
        if(minute<10) minute_string = "0"+minute_string;

        String time = (hourOfDay < 12) ? hour_string + ":"+minute_string+" AM" : Integer.toString(hourOfDay-12) + ":"+minute_string+" PM";
        this.time = time;
        timeBtn.setText(time);
    }
}
