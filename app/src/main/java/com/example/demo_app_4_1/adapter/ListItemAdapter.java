package com.example.demo_app_4_1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo_app_4_1.R;
import com.example.demo_app_4_1.model.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ItemViewHolder> {
    private Context context ;
    public List<Item> itemList = new ArrayList<>();
    public List<Item> setItemList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    public ListItemAdapter(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtId, txtName, txtPrice;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgView);
            txtId = itemView.findViewById(R.id.tv_id_item);
            txtName = itemView.findViewById(R.id.tv_name_item);
            txtPrice = itemView.findViewById(R.id.tv_price_item);
        }

        public void onBindData(Item item){
            imageView.setImageURI(Uri.parse(item.getResourceImage()));
            txtId.setText(item.getId()+"");
            txtName.setText(item.getName()+"");
            txtPrice.setText(item.getPrice()+"");

            setBgr();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dia = new AlertDialog.Builder(v.getContext());
                    dia.setTitle("Dialog Fragment");
                    dia.setIcon(R.drawable.baseline_language_black_24);
                    dia.setMessage("ID" + item.getId() + ": " + item.getName() + ", "
                            + item.getPrice());

                    dia.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    dia.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dia.show();

                    v.setBackgroundResource(R.drawable.bg_green_corner10);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id", item.getId());
                    editor.putString("image", item.getResourceImage());
                    editor.putString("name", item.getName());
                    editor.putInt("price", item.getPrice());
                    editor.putBoolean("favorite", item.isFavorite());
                    editor.apply();
                }
            });
        }

        private void setBgr(){
            itemView.setBackgroundResource(R.drawable.bg_white_corner10);
        }

    };

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        if(item == null){
            return;
        }
        holder.onBindData(item);
    }

    @Override
    public void onViewRecycled(@NonNull ItemViewHolder holder) {
        super.onViewRecycled(holder);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public int getItemCount() {
        if(itemList != null){
            return itemList.size();
        }
        return 0;
    }

    public void setData(List<Item> List){
        this.itemList = List;

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                int idChanged = 0;
                idChanged = sharedPreferences.getInt("id", 0);
                if (key.equals("id")) {
                    idChanged = sharedPreferences.getInt(key, 0);
                    for (Item item : itemList) {
                        if (item.getId() == idChanged) {
                            item.setId(idChanged);
                            break;
                        }
                    }
                }

                if (key.equals("name")) {
                    String nameChanged = sharedPreferences.getString(key, "");
                    for (Item item : itemList) {
                        if (item.getId() == idChanged) {
                            item.setName(nameChanged);
                            break;
                        }
                    }
                }
                if (key.equals("image")) {
                    String imageChanged = sharedPreferences.getString(key, "");
                    for (Item item : itemList) {
                        if (item.getId() == idChanged) {
                            item.setResourceImage(imageChanged);
                            break;
                        }
                    }
                }
                if (key.equals("price")) {
                    int priceChanged = sharedPreferences.getInt(key, 0);
                    for (Item item : itemList) {
                        if (item.getId() == idChanged) {
                            item.setPrice(priceChanged);
                            break;
                        }
                    }
                }
                if (key.equals("favorite")) {
                    boolean favorite = sharedPreferences.getBoolean(key, false);
                    for (Item item : itemList) {
                        if (item.getId() == idChanged) {
                            item.setFavorite(favorite);
                            break;
                        }
                    }
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        notifyDataSetChanged();
    }

    public void fillData(List<Item> listFavor){
        List<Item> newList = new ArrayList<>(listFavor);
        this.itemList = newList;
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                boolean checkFavor = sharedPreferences.getBoolean("favorite", false);

                if (key.equals("favorite")){
                    String imageChanged = sharedPreferences.getString("image", "");
                    int idCha = sharedPreferences.getInt("id", 0);
                    String nameCha = sharedPreferences.getString("name", "");
                    int priceChanged = sharedPreferences.getInt("price", 0);

                    if (listFavor != null){
                        if (checkFavor){
                            int count = 0;
                            for (Item item : listFavor){
                                if (item.getId() == idCha){
                                    count++;
                                }
                            }
                            if (count == 0 ){
                                listFavor.add(new Item(imageChanged, idCha, nameCha, priceChanged, checkFavor));
                            }
                        }
                        else {
                            Iterator<Item> iterator = listFavor.iterator();
                            while (iterator.hasNext()) {
                                Item item = iterator.next();
                                if (item.getId() == idCha) {
                                    iterator.remove();
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        if (checkFavor){
                            listFavor.add(new Item(imageChanged, idCha, nameCha, priceChanged, checkFavor));
                        }
                    }
                }
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        notifyDataSetChanged();
    }
}
