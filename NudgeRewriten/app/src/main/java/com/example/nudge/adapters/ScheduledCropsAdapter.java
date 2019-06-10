package com.example.nudge.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nudge.R;
import com.example.nudge.activities.CropTimeline;
import com.example.nudge.models.CropModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ScheduledCropsAdapter extends RecyclerView.Adapter<ScheduledCropsAdapter.MyCustomViewHolder> {


    List<CropModel> crops;
    Context context;
    String id;

    public ScheduledCropsAdapter(List<CropModel> crops, Context context,String id) {
        this.crops = crops;
        this.context = context;
        this.id = id;
    }

    @NonNull
    @Override
    public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.scheduledcrop,viewGroup,false);
        return new MyCustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder myCustomViewHolder, final int i) {

        myCustomViewHolder.cropName.setText(crops.get(i).getCropname());

        myCustomViewHolder.cropId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CropTimeline.class);
                intent.putExtra("CROPID",crops.get(i).getId());
                intent.putExtra("FARMERID",id);
                context.startActivity(intent);
            }
        });

        Picasso.get().load(crops.get(i).getCropImage()).into(myCustomViewHolder.cropImage);
    }

    @Override
    public int getItemCount() {
        return crops.size();
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder {

        ImageView cropImage;
        TextView cropName;
        ConstraintLayout cropId;

        public MyCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            cropImage = itemView.findViewById(R.id.crop_image);
            cropName = itemView.findViewById(R.id.crop_name);
            cropId = itemView.findViewById(R.id.crop_id);
        }
    }
}
