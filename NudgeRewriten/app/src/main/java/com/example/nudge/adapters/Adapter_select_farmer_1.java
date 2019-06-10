package com.example.nudge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nudge.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_select_farmer_1 extends RecyclerView.Adapter<Adapter_select_farmer_1.Adapter_select_farmer_1_ViewHolder> {

    public List<String> data=new ArrayList<>();
    Context context;
    public Adapter_select_farmer_1(List<String> data,Context context){
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public Adapter_select_farmer_1_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_farmer_list_1,viewGroup,false);
        return new Adapter_select_farmer_1_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_select_farmer_1_ViewHolder adapter_select_farmer_1_viewHolder, int i) {
        int count=0;
        ArrayList<String> cardList = new ArrayList<String>();
        char c=data.get(0).charAt(0),c2;
        for (String cont:
                data) {
            c2=cont.charAt(0);
            if (c!=c2){
                count++;
                c=c2;
            }
            if (count == i+1)
                break;

            if (count==i){
                cardList.add(cont);
            }

        }
        adapter_select_farmer_1_viewHolder.contact_list_recycler.setAdapter(new Adapter_select_farmer_2(cardList,context));
        adapter_select_farmer_1_viewHolder.contact_list_alphabet.setText(String.valueOf(cardList.get(0).charAt(0)));
        Log.d("fire", "onBindViewHolder: "+cardList);

    }

    @Override
    public int getItemCount() {
        int count=0;
        char c=data.get(0).charAt(0),c2;
        for (String cont: data) {
            c2=cont.charAt(0);
            if (c!=c2){
                count++;
                c=c2;
            }

        }
        return count+1;
    }

    public class Adapter_select_farmer_1_ViewHolder extends RecyclerView.ViewHolder{

        TextView contact_list_alphabet;
        CardView contact_list_card;
        RecyclerView contact_list_recycler;


        public Adapter_select_farmer_1_ViewHolder(@NonNull View itemView) {
            super(itemView);
            contact_list_recycler = itemView.findViewById(R.id.contact_list_recycler);
            contact_list_recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            contact_list_card = itemView.findViewById(R.id.contact_list_card);
            contact_list_alphabet = itemView.findViewById(R.id.contact_list_alphabet);
        }
    }
}
