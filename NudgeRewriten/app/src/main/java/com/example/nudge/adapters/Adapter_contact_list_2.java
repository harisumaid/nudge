package com.example.nudge.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nudge.R;
import com.example.nudge.activities.FarmerProfileActivity;
import com.example.nudge.models.FarmerModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class Adapter_contact_list_2 extends RecyclerView.Adapter<Adapter_contact_list_2.Adapter_contact_list_2_ViewHolder> {

    String[] contactList;
    List<FarmerModel> farmers;
    Context context;


    public Adapter_contact_list_2(String[] contactList, List<FarmerModel> farmers, Context context) {
        this.contactList = contactList;
        this.farmers = farmers;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_contact_list_2_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.adapter_farmer_list_2,viewGroup,false);
        return new Adapter_contact_list_2_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_contact_list_2_ViewHolder adapter_contact_list_2_viewHolder, final int i) {
        adapter_contact_list_2_viewHolder.contact_in_card.setText(contactList[i]);

        adapter_contact_list_2_viewHolder.farmerId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),FarmerProfileActivity.class);
                intent.putExtra("FarmerId",farmers.get(i).getId());
                intent.putExtra("FarmerInfo",farmers.get(i));
                v.getContext().startActivity(intent);
            }
        });

        Glide.with(context).load(farmers.get(i).getImage()).into(adapter_contact_list_2_viewHolder.farmerImg);

        if (i==((contactList.length)-1)){
            adapter_contact_list_2_viewHolder.line_divider.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return contactList.length;
    }

    public class Adapter_contact_list_2_ViewHolder extends RecyclerView.ViewHolder{
        TextView contact_in_card;
        ImageView farmerImg;
        View line_divider;
        CardView farmerId;

        public Adapter_contact_list_2_ViewHolder(@NonNull View itemView) {
            super(itemView);
            line_divider = itemView.findViewById(R.id.line_divider);
            contact_in_card = itemView.findViewById(R.id.contact_in_card);
            farmerId = itemView.findViewById(R.id.farmer_id);
            farmerImg = itemView.findViewById(R.id.farmer_img);
        }
    }
}
