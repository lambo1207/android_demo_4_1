package com.example.demo_app_4_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.gun0912.*;

import java.util.List;

public class DetailItemFragment extends Fragment {

    ImageView imageView, imgFavorite;
    TextView txtId;
    EditText edtName, edtPrice;
    Button btnUpdate;

    private int id;
    private String img;
    private String name;
    private int price;

    private boolean favorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_item, container, false);

        imageView = view.findViewById(R.id.imgViewDetail);
        imgFavorite =  view.findViewById(R.id.btnFavorite);
        txtId = view.findViewById(R.id.txt_id_item_detail);
        edtName = view.findViewById(R.id.edt_name_item_detail);
        edtPrice = view.findViewById(R.id.edt_price_item_detail);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        setData();

        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorite){
                    favorite = false;
                    imgFavorite.setImageResource(R.drawable.baseline_favorite_24);
                }
                else {
                    favorite = true;
                    imgFavorite.setImageResource(R.drawable.baseline_favorite_24_white);
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int idUpdate = Integer.parseInt(txtId.getText().toString());
                Uri uri = Uri.parse(img);
                String nameUpdate = edtName.getText().toString();
                int priceUpdate = Integer.parseInt(edtPrice.getText().toString());

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id", idUpdate);
                editor.putString("image", uri.toString());
                editor.putString("name", nameUpdate);
                editor.putInt("price", priceUpdate);
                editor.putBoolean("favorite", favorite);
                editor.apply();

                Toast.makeText(getContext(), "Update success!!!", Toast.LENGTH_SHORT).show();

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPermission();
            }
        });

        return view;
    }

    private void selectImageFromGallery(){
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();
            String imgPath = uri.toString();

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("image", imgPath);
            editor.apply();

            imageView.setImageURI(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void callPermission(){

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImageFromGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener;
    void setData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);


        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals("id")){
                    int idChanged = sharedPreferences.getInt(key, 0);
                    txtId.setText(idChanged + "");
                }
                if(key.equals("name")){
                    String nameChanged = sharedPreferences.getString(key, "");
                    edtName.setText(nameChanged);
                }
                if(key.equals("image")){
                    String imageChanged = sharedPreferences.getString(key, "");
                    Uri uri = Uri.parse(imageChanged);
                    imageView.setImageURI(uri);
                    edtName.setText(imageChanged);
                }
                if(key.equals("price")){
                    int priceChanged = sharedPreferences.getInt(key, 0);
                    edtPrice.setText(priceChanged + "");
                }
                if(key.equals("favorite")){
                    boolean favoriteChanged = sharedPreferences.getBoolean(key, false);
                    if(favoriteChanged){
                        imgFavorite.setImageResource(R.drawable.baseline_favorite_24);
                    }
                    else {
                        imgFavorite.setImageResource(R.drawable.baseline_favorite_24_white);
                    }
                }
            }
        };

        id = sharedPreferences.getInt("id", 0);
        img = sharedPreferences.getString("image", "");
        name = sharedPreferences.getString("name", "");
        price = sharedPreferences.getInt("price", 0);
        favorite = sharedPreferences.getBoolean("favorite", false);

        txtId.setText(String.valueOf(id));

        edtName.setText(name);
        edtPrice.setText(String.valueOf(price));

        if (!img.isEmpty()) {
            Uri uri = Uri.parse(img);
            imageView.setImageURI(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        setData();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

}