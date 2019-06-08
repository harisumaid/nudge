package com.example.nudgerewriten.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nudgerewriten.R;


public class ScheduleCropAdapter  extends RecyclerView.Adapter<ScheduleCropAdapter.ScheduleCropHolder> {

    int count;


    public ScheduleCropAdapter(int count) {
        this.count = count;
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

        int x =i+1;
        if(x > 1) {

            viewHolder.cropno.setText("Add Crop " + x);
        }
        else {
            viewHolder.cropno.setText("Add Crop");
        }
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

        TextView cropno;
        final EditText farmSizeId;
        public ScheduleCropHolder(@NonNull View itemView) {
            super(itemView);

            cropno = (TextView) itemView.findViewById(R.id.textView2);
            farmSizeId = (EditText) itemView.findViewById(R.id.farm_size_id);
        }
    }

    public void updateCount( int count)
    {
        this.count = count;
        notifyDataSetChanged();
    }
}
