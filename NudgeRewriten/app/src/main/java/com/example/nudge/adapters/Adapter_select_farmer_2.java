package com.example.nudge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nudge.R;
import com.example.nudge.activities.SelectFarmerActivity;
import com.example.nudge.models.FarmerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_select_farmer_2 extends RecyclerView.Adapter<Adapter_select_farmer_2.Adapter_select_farmer_2_ViewHolder> {

    List<String> contactList;
    static List<String> ids= new ArrayList<>();
    List<FarmerModel> farmers = new ArrayList<>();
    Context context;
    SelectFarmerActivity selectFarmerActivity;
    int flag=0;

    public Adapter_select_farmer_2(List<String> contactList,List<FarmerModel> farmers, Context context){
        this.context=context;
        this.contactList=contactList;
        this.farmers = farmers;
        selectFarmerActivity = (SelectFarmerActivity) context;
        ids.clear();
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

        if(flag==1) {
            Toast.makeText(context, "Orders are recorded.", Toast.LENGTH_SHORT).show();
        } else {

            adapter_select_farmer_2_viewHolder.select_farmer_name.setText(farmers.get(i).getName());

            // Setting image of the contact here
            Glide.with(context).load(farmers.get(i).getImage()).into(adapter_select_farmer_2_viewHolder.select_farmer_image);

            Log.d("fire", "onBindViewHolder: "+contactList);

            adapter_select_farmer_2_viewHolder.select_farmer_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "Card is clicked.", Toast.LENGTH_SHORT).show();

                    if ( adapter_select_farmer_2_viewHolder.select_farmer_checkbox.isChecked()) {

                        adapter_select_farmer_2_viewHolder.select_farmer_checkbox.setChecked(false);
                        ids.remove(farmers.get(i).getId());
                    }
                    else
                        {
                            adapter_select_farmer_2_viewHolder.select_farmer_checkbox.setChecked(true);
                            ids.add(farmers.get(i).getId());
                            Toast.makeText(context, "Total ids are "+ids.size(), Toast.LENGTH_SHORT).show();
                        }
                }
            });

// storing what item is checked and changing the corresponding flag in selectFarmerActivity
            adapter_select_farmer_2_viewHolder.select_farmer_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        Log.d("Checked", String.valueOf(i));
                        selectFarmerActivity.flag[i] = true;
                    }
                    else {
                        selectFarmerActivity.flag[i]= false;
                    }
                }
            });


            if (i==((contactList.size())-1)){
                adapter_select_farmer_2_viewHolder.select_farmer_divider.setVisibility(View.GONE);
            }

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
        CardView select_farmer_cardview;

        public Adapter_select_farmer_2_ViewHolder(@NonNull View itemView) {
            super(itemView);
            select_farmer_divider= itemView.findViewById(R.id.select_farmer_divider);
            select_farmer_name=itemView.findViewById(R.id.select_farmer_name);
            select_farmer_image=itemView.findViewById(R.id.select_farmer_image);
            select_farmer_checkbox = itemView.findViewById(R.id.select_farmer_checkbox);
            select_farmer_cardview = itemView.findViewById(R.id.select_farmer_cardview);
        }
    }

    public void uploadOrders() {
        flag=1;
        notifyDataSetChanged();
    }

    public List<String> uploadVisits() {
        flag=2;
        Toast.makeText(context, "Total ids are "+ids.size(), Toast.LENGTH_SHORT).show();
        return ids;
    }
}
