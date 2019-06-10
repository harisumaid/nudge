package com.example.nudgerewriten.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nudgerewriten.R;
import com.example.nudgerewriten.activities.SelectFarmerActivity;

import java.util.ArrayList;
import java.util.List;

public class Adapter_select_farmer_2 extends RecyclerView.Adapter<Adapter_select_farmer_2.Adapter_select_farmer_2_ViewHolder> {

    List<String> contactList;
    Context context;
    SelectFarmerActivity selectFarmerActivity;

    public Adapter_select_farmer_2(List<String> contactList,Context context){
        this.context=context;
        this.contactList=contactList;
        selectFarmerActivity = (SelectFarmerActivity) context;
    }
    @NonNull
    @Override
    public Adapter_select_farmer_2_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_select_farmer_2,viewGroup,false);
        return new Adapter_select_farmer_2_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_select_farmer_2_ViewHolder adapter_select_farmer_2_viewHolder, final int i) {
        final int currIndex=selectFarmerActivity.select_farmer.indexOf(contactList.get(i));

        adapter_select_farmer_2_viewHolder.select_farmer_name.setText(contactList.get(i));
        Log.d("fire", "onBindViewHolder: "+contactList);

//  if a checkbox was checked previously it remains checked
        if ( selectFarmerActivity.flag[currIndex] )
            adapter_select_farmer_2_viewHolder.select_farmer_checkbox.setChecked(true);

        else
            adapter_select_farmer_2_viewHolder.select_farmer_checkbox.setChecked(false);


// storing what item is checked and changing the corresponding flag in selectFarmerActivity
        adapter_select_farmer_2_viewHolder.select_farmer_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                     selectFarmerActivity.flag[currIndex] = true;
                }
                else {
                    selectFarmerActivity.flag[currIndex]= false;
                }
            }
        });


// Setting image of the contact here


        if (i==((contactList.size())-1)){
            adapter_select_farmer_2_viewHolder.select_farmer_divider.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class Adapter_select_farmer_2_ViewHolder extends RecyclerView.ViewHolder{
        TextView select_farmer_name;
        ImageView select_farmer_image;
        CheckBox select_farmer_checkbox;
        View select_farmer_divider;


        public Adapter_select_farmer_2_ViewHolder(@NonNull View itemView) {
            super(itemView);
            select_farmer_divider= itemView.findViewById(R.id.select_farmer_divider);
            select_farmer_name=itemView.findViewById(R.id.select_farmer_name);
            select_farmer_image=itemView.findViewById(R.id.select_farmer_image);
            select_farmer_checkbox = itemView.findViewById(R.id.select_farmer_checkbox);
        }
    }
}
