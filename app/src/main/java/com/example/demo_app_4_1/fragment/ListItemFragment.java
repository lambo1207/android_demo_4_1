package com.example.demo_app_4_1.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo_app_4_1.adapter.ListItemAdapter;
import com.example.demo_app_4_1.R;
import com.example.demo_app_4_1.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListItemFragment extends Fragment {

    RecyclerView rcvItem;
    List<Item> itemList = new ArrayList<>();
    ListItemAdapter listItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_item, container, false);

        rcvItem = view.findViewById(R.id.rcvListItem);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvItem.setLayoutManager(linearLayoutManager);

        listItemAdapter = new ListItemAdapter(getActivity());
        itemList = getData(itemList, getContext());
        listItemAdapter.setData(itemList);
        rcvItem.setAdapter(listItemAdapter);
        listItemAdapter.notifyDataSetChanged();
        return view;
    }

    public List<Item> getData(List<Item> itemList, Context context) {
        Random random = new Random();
        int[] imgInt = {R.drawable.ipad1, R.drawable.ipad2, R.drawable.iphone2, R.drawable.iphone22, R.drawable.iphone23, R.drawable.iphone24, R.drawable.iphone30, R.drawable.iphone31};
        String[] imgString = new String[imgInt.length];
        String imgParse;
        for (int i = 0; i < imgInt.length; i++) {
            imgParse = "android.resource://" + context.getPackageName() + "/" + imgInt[i];
            imgString[i] = imgParse;
        }
        String[] name = {"Iphone 11", "Iphone 12 Pro", "Iphone 13 ProMax", "Iphone 12 Mini", "Iphone 13 Mini", "Iphone 11 ProMax", "Iphone 14", "Iphone 14 Pro", "Iphone 14 ProMax", "Iphone 15"};
        int[] price = {12000000, 16000000, 15000000, 17000000, 18000000, 19000000, 20000000, 22000000, 25000000, 26000000, 30000000};

        for (int i = 1; i <= 5; i++){
            itemList.add(new Item( imgString[random.nextInt(imgString.length)], i,  name[random.nextInt(name.length)], price[random.nextInt(price.length)], false));
        }
        return itemList;
    }
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onResume() {
        super.onResume();
        listItemAdapter.setData(itemList);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}