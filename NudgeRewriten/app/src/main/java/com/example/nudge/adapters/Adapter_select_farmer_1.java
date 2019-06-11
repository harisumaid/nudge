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
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.models.FarmerModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Adapter_select_farmer_1 extends RecyclerView.Adapter<Adapter_select_farmer_1.Adapter_select_farmer_1_ViewHolder> {

    List<String> data=new ArrayList<>();
    List<FarmerModel> list = new ArrayList<>();
    Context context;
    Set<Character> hash_Set = new HashSet<>();
    Adapter_select_farmer_2 adapter;

    public Adapter_select_farmer_1(List<String> data,List<FarmerModel> farmers,Context context){

        this.context=context;
        this.data=data;
        list = farmers;

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

        String[] newData = new String[data.size()];
        int index=0;
        for(String d: data)
        {       newData[index]=d.trim();
            index++;
        }

        for(int ind=0;ind<newData.length;ind++)
            Log.i("Data is ",newData[ind]);

        ArrayList<String> cardList = new ArrayList<String>();
        List<FarmerModel> farmers = new ArrayList<>();
        hash_Set.clear();
        for(String c: newData) {
            hash_Set.add(c.charAt(0));
        }

        Set<Character> chars1 = new TreeSet<>(hash_Set);
        List<Character> chars = new ArrayList<>(chars1);

        Log.i("Data", "onBindViewHolder: " + chars);
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

        adapter = new Adapter_select_farmer_2(cardList,farmers,context);
        adapter_select_farmer_1_viewHolder.contact_list_recycler.setAdapter(adapter);

        adapter_select_farmer_1_viewHolder.contact_list_alphabet.setText(String.valueOf(cardList.get(0).charAt(0)));

    }

    @Override
    public int getItemCount() {
        hash_Set.clear();

        for(String c: data) {
            hash_Set.add(c.charAt(0));
        }
        return hash_Set.size();
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

    public List<String> getNames() {
        Toast.makeText(context, "Adapter1 function exec", Toast.LENGTH_SHORT).show();
        List<String> names = adapter.uploadVisits();
        Log.d("Size is", String.valueOf(names.size()));
        return adapter.uploadVisits();
    }

}
