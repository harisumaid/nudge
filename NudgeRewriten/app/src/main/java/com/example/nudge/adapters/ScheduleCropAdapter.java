package com.example.nudge.adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.activities.FarmerProfileActivity;
import com.example.nudge.activities.ScheduleCropActivity;
import com.example.nudge.fragments.DatePickerFragment;
import com.example.nudge.models.CropModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleCropAdapter extends RecyclerView.Adapter<ScheduleCropAdapter.ScheduleCropHolder> {

    String id;
    int count=1;
    FirebaseFirestore db;
    int flag=0;

    Context context;
    SharedPrefUtils sharedPrefUtils;

    public ScheduleCropAdapter(String id, Context context) {
        this.id = id; this.context = context;
        db = FirebaseFirestore.getInstance();
        sharedPrefUtils = new SharedPrefUtils(context);
    }

    @NonNull
    @Override

    public ScheduleCropHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View inflater = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.acitivity_schedule_crop_recycler,viewGroup,false);
        ScheduleCropHolder scheduleCropHolder = new ScheduleCropHolder(inflater);
        return scheduleCropHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleCropHolder viewHolder, int i) {

        if(flag==1) {

            DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);

            Date date = null;
            try {
                date = df.parse(viewHolder.cropScheduleDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String cropName = viewHolder.nameText.getSelectedItem().toString();
            String cropVariety = viewHolder.varietyText.getSelectedItem().toString();
            String size = viewHolder.farmSizeId.getText().toString();

            CropModel crop = new CropModel(cropName,cropVariety,size,"Noimage","",date);

            final CollectionReference crops = db.collection("agents/"+ sharedPrefUtils.readAgentId() +"/farmers/"+id+"/crops");

            crops.add(crop).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    crops.document(documentReference.getId()).update(
                      "id",documentReference.getId(),
                            "cropImage","https://www.ruralmarketing.in/sites/default/files/styles/large/public/odisha-to-have-a-new-agriculture-technology-park-at-rourkela.jpg?itok=RH-zOiuR"
                    );
                    Toast.makeText(context, "Crops has been added.", Toast.LENGTH_SHORT).show();
                    ((ScheduleCropActivity)context).onBackPressed();
                }
            });
            Log.i("Crops are ", crop.getSeedingDate().toString());
        }

        viewHolder.cropScheduleDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                DatePickerDialog mDatePicker;

                mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar cal = Calendar.getInstance(); cal.setTimeInMillis(0);
                        cal.set(year, month, dayOfMonth, 0, 0, 0);
                        Date chosenDate = cal.getTime();
                        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
                        String formattedDate = df.format(chosenDate);

                        viewHolder.cropScheduleDate.setText(formattedDate);
                    }
                },2019,5,1);

                mDatePicker.show();
            }
        });

        viewHolder.farmSizeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.farmSizeId.setCursorVisible(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class ScheduleCropHolder extends RecyclerView.ViewHolder{

        EditText farmSizeId;
        TextView cropScheduleDate;
        Spinner varietyText,nameText;

        public ScheduleCropHolder(@NonNull View itemView) {
            super(itemView);

            varietyText = itemView.findViewById(R.id.crop_variety);
            nameText = itemView.findViewById(R.id.crop_name);
            farmSizeId = itemView.findViewById(R.id.farm_size_id);
            cropScheduleDate = itemView.findViewById(R.id.crop_schedule_date);
        }
    }

    public void updateCount(int count)
    {
        this.count = count;
        notifyDataSetChanged();
    }

    public void uploadCropDetails() {
        flag=1;
        notifyDataSetChanged();
    }
}
