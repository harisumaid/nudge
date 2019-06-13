package com.example.nudge.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nudge.R;
import com.example.nudge.activities.ViewStore;
import com.example.nudge.models.products;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHolder> {

    List<products> products;
    Context context;

    public ProductAdapter(List<com.example.nudge.models.products> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.prod_layout_home, viewGroup, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, final int i) {

        myViewHolder.prod_id.setText(products.get(i).getProduct_name());
        Glide.with(context).load(products.get(i).getProduct_image()).into(myViewHolder.prod_image);

        myViewHolder.prod_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewStore.class);
                intent.putExtra("POSITION",i);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView prod_id;
        ImageView prod_image;

        public myViewHolder(@NonNull View itemView) {

            super(itemView);
            prod_image = itemView.findViewById(R.id.prod_image);
            prod_id = itemView.findViewById(R.id.prod_id);
        }
    }
}
