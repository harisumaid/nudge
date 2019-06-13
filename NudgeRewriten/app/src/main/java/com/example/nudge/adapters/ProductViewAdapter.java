package com.example.nudge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nudge.R;
import com.example.nudge.activities.ViewStore;
import com.example.nudge.models.products;

import java.util.List;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ProductViewHolder> {

    public List<products> Products;
    ViewStore viewStore;
    Context context;
    static int currentPosition= -1;

    public ProductViewAdapter(List<products> products, Context context) {
        this.Products = products;
        this.context = context;
        viewStore = (ViewStore) context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_view_store_list,viewGroup,false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view,viewStore);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder viewHolder, final int i) {
        viewHolder.itemimage.setImageResource(Products.get(i).getProductimage());
        viewHolder.Price.setText(Integer.toString(Products.get(i).getPrice()));
        viewHolder.Productname.setText(Products.get(i).getProductname());
        viewHolder.Productcompany.setText(Products.get(i).getProductComany());
        viewHolder.viewStoreChildView.setVisibility(View.GONE);
        viewHolder.viewstoreDropDownIcon.setImageResource(R.drawable.ic_expand_more_black_24dp);
        viewHolder.viewstore_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewStore.productName=(String) viewHolder.Productname.getText();
                viewStore.showPopUps(v);
            }
        });

        if(currentPosition == i) {
            viewHolder.viewStoreChildView.setVisibility(View.VISIBLE);
            viewHolder.viewstoreDropDownIcon.setImageResource(R.drawable.ic_expand_less_black_24dp);
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
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
        return Products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder
    {

        ImageView itemimage,viewstoreDropDownIcon;
        TextView Productname,Productcompany,Price,VisitDate;
        ViewStore viewStore;
        CardView cardView;
        ConstraintLayout viewStoreChildView;
        MaterialButton viewstore_order_button;


        public ProductViewHolder(@NonNull View itemView, ViewStore viewStore) {
            super(itemView);
            VisitDate=itemView.findViewById(R.id.visit_date);
            viewstoreDropDownIcon = itemView.findViewById(R.id.viewstore_dropDownIcon);
            itemimage=(ImageView) itemView.findViewById(R.id.viewstore_product_image);
            Productname=(TextView) itemView.findViewById(R.id.viewstore_product_title);
            Productcompany =(TextView) itemView.findViewById(R.id.viewstore_company_name);
            Price=(TextView) itemView.findViewById(R.id.viewstore_product_price);
            cardView = (CardView) itemView.findViewById(R.id.viewstore_order_card);
            this.viewStore = viewStore;
            viewStoreChildView=itemView.findViewById(R.id.viewstore_child_view);
            viewstore_order_button=itemView.findViewById(R.id.viewstore_order_button);
        }

    }
}
