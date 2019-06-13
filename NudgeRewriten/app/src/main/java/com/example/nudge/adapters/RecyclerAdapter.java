package com.example.nudge.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nudge.R;
import com.example.nudge.activities.SelectFarmerActivity;
import com.example.nudge.activities.SelectedCropActity;
import com.example.nudge.models.crop;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends Adapter<RecyclerAdapter.ImageViewHolder> {

    List<crop> cropList= new ArrayList<>();
    Context context;

    public RecyclerAdapter(List<crop> crops, Context context){
        this.context =context;
        this.cropList=crops;
    }
    @Nullable
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.croplist,viewGroup,false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder viewHolder, final int i) {


        viewHolder.croptitle.setText(cropList.get(i).getName());

        Glide.with(context).load(cropList.get(i).getImage()).into(viewHolder.crop);

        viewHolder.crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(context!=null)
                {

                    Intent intent = new Intent(context, SelectedCropActity.class);
                    intent.putExtra("ID",cropList.get(i).getId());
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cropList.size();
    }

    public static class  ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView crop;
        TextView croptitle,cropDesc;

        public ImageViewHolder(@NonNull final View itemView) {
            super(itemView);
            crop=  (ImageView) itemView.findViewById(R.id.crop);
            croptitle=(TextView) itemView.findViewById(R.id.croptitle);
            cropDesc = itemView.findViewById(R.id.crop_desc);
        }
    }

}