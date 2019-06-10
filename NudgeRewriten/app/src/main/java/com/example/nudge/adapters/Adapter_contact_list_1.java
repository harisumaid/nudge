package com.example.nudge.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.models.FarmerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static android.support.constraint.Constraints.TAG;

public class Adapter_contact_list_1 extends RecyclerView.Adapter<Adapter_contact_list_1.Adapter_contact_list_1_ViewHolder> {

    List<FarmerModel> list;
    List<String> data;
    List<Character> initials=new ArrayList<>();
    Context context;
    Set<Character> hash_Set = new HashSet<>();

    public Adapter_contact_list_1(List<String> data, List<FarmerModel> list, Context context) {

        this.data = data;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_contact_list_1_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_farmer_list_1,viewGroup,false);
        return new Adapter_contact_list_1_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_contact_list_1_ViewHolder adapter_contact_list_1_viewHolder, int i) {


        String[] newData = new String[data.size()];
        int index=0;
        for(String d: data)
        {       newData[index]=d.trim();
                index++;
            }

        Arrays.sort(newData);

        for(int ind=0;ind<newData.length;ind++)
            Log.i("Data is ",newData[ind]);

        ArrayList<String> cardList = new ArrayList<String>();
        List<FarmerModel> farmers = new ArrayList<>();
        hash_Set.clear();
        for(String c: newData) {
            hash_Set.add(c.charAt(0));
        }

        List<Character> chars = new ArrayList<>(hash_Set);

        index = 0;
        for(String name: newData) {
              if(name.charAt(0)==chars.get(i))
              {
                  cardList.add(name);
                  farmers.add(list.get(index));
              }
              index++;
        }

        for(String d: cardList)
            Log.i("Names are : ",chars.get(i)+" " +d);

        adapter_contact_list_1_viewHolder.contact_list_recycler.setAdapter(new Adapter_contact_list_2((cardList.toArray(new String[(cardList.size())])),farmers,context));

        adapter_contact_list_1_viewHolder.contact_list_alphabet.setText(String.valueOf(cardList.get(0).charAt(0)));

    }

    @Override
    public int getItemCount() {
        hash_Set.clear();

        for(String c: data) {
            hash_Set.add(c.charAt(0));
        }

        return hash_Set.size();
    }

    public class Adapter_contact_list_1_ViewHolder extends RecyclerView.ViewHolder{

        TextView contact_list_alphabet;
        CardView contact_list_card;
        RecyclerView contact_list_recycler;

        public Adapter_contact_list_1_ViewHolder(View itemView){
            super(itemView);
            contact_list_recycler = itemView.findViewById(R.id.contact_list_recycler);
            contact_list_recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            contact_list_card = itemView.findViewById(R.id.contact_list_card);
            contact_list_alphabet = itemView.findViewById(R.id.contact_list_alphabet);
        }
    }
}
