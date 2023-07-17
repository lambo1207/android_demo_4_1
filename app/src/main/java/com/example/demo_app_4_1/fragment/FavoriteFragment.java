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
import android.widget.Toast;

import com.example.demo_app_4_1.adapter.ListItemAdapter;
import com.example.demo_app_4_1.R;
import com.example.demo_app_4_1.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FavoriteFragment extends Fragment {

    RecyclerView rcvItem;
    List<Item> itemList = new ArrayList<>();
    ListItemAdapter listItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        rcvItem = view.findViewById(R.id.rcvFavorite);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvItem.setLayoutManager(linearLayoutManager);

        listItemAdapter = new ListItemAdapter(getActivity());
        listItemAdapter.fillData(itemList);
        rcvItem.setAdapter(listItemAdapter);
        listItemAdapter.notifyDataSetChanged();

        return view;
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onResume() {
        super.onResume();
        listItemAdapter.fillData(itemList);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}