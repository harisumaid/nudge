package com.example.nudge.adapters;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nudge.R;
import com.example.nudge.activities.FarmerProfileActivity;
import com.example.nudge.models.FarmerModel;
import com.example.nudge.models.VisitModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitsAdapter extends RecyclerView.Adapter<VisitsAdapter.myViewHolder> {

    Context context;
    static int currentPosition = -1;
    List<VisitModel> visits = new ArrayList<>();
    String phone_num,id;
    FirebaseFirestore db;
    SharedPrefUtils sharedPrefUtils;

    public VisitsAdapter(List<VisitModel> visits, Context context) {
        this.visits = visits;
        this.context = context;
        sharedPrefUtils = new SharedPrefUtils(context);
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public VisitsAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.farmers, viewGroup, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VisitsAdapter.myViewHolder myViewHolder, final int i) {

        db.collection("agents").document(sharedPrefUtils.readAgentId()).collection("farmers").document(visits.get(i).getFarmer_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                FarmerModel farmer = documentSnapshot.toObject(FarmerModel.class);
                myViewHolder.farmerName.setText(farmer.getName());
                phone_num = farmer.getPrimary_contact_number();
                id = farmer.getId();
                Glide.with(context).load(farmer.getImage()).into(myViewHolder.farmerImage);
            }
        });

        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
        String formattedDate = df.format(visits.get(i).getVisitDate());

        myViewHolder.visitDate.setText(formattedDate);
        myViewHolder.visitTitle.setText(visits.get(i).getVisitTitle());
        myViewHolder.visitChildView.setVisibility(View.GONE);

        myViewHolder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + phone_num ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                context.startActivity(intent);
            }
        });

        myViewHolder.profileTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("agents").document(sharedPrefUtils.readAgentId()).collection("farmers").document(visits.get(i).getFarmer_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Intent intent = new Intent(context, FarmerProfileActivity.class);
                        intent.putExtra("FarmerInfo",documentSnapshot.toObject(FarmerModel.class));
                        context.startActivity(intent);
                    }
                });
            }
        });

        myViewHolder.changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "It is here.", Toast.LENGTH_SHORT).show();

                db.collection("agents").document(sharedPrefUtils.readAgentId()).collection("visits").document(visits.get(i).getId()).update(
                  "visit_status",true
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Status changed", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error is ",e.toString());
                    }
                });
            }
        });

        if(currentPosition == i) {
            myViewHolder.visitChildView.setVisibility(View.VISIBLE);
        }

        myViewHolder.visitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPosition == i) currentPosition = -1;
                else
                    currentPosition = i;
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return visits.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        CircleImageView farmerImage;
        TextView visitTitle,farmerName,visitDate,profileTextBtn,changeStatusBtn;
        ConstraintLayout visitChildView;
        CardView visitCard;
        ImageView callBtn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            profileTextBtn = itemView.findViewById(R.id.profile_text_btn);
            changeStatusBtn = itemView.findViewById(R.id.change_status_btn);
            callBtn = itemView.findViewById(R.id.call_btn);
            farmerImage = itemView.findViewById(R.id.farmer_image);
            visitTitle = itemView.findViewById(R.id.visit_title);
            farmerName = itemView.findViewById(R.id.farmer_name);
            visitDate = itemView.findViewById(R.id.visit_date);
            visitCard = itemView.findViewById(R.id.visit_order_card);
            visitChildView = itemView.findViewById(R.id.visit_child_view);
        }
    }
}
