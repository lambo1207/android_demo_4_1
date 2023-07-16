package com.example.demo_app_4_1.fragment;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

import com.example.demo_app_4_1.MainActivity;
import com.example.demo_app_4_1.R;
import com.example.demo_app_4_1.notification.MyNotification;
import com.example.demo_app_4_1.service.NotifiService;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.Date;
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
    private boolean favorite = false;
    private static final String TITLE_NOTIFI = "Add favorite";
    private static final String CONTENT_NOTIFI = "The product has been added to favorites";
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_item, container, false);

        imageView = view.findViewById(R.id.imgViewDetail);
        imgFavorite = view.findViewById(R.id.btnFavorite);
        txtId = view.findViewById(R.id.txt_id_item_detail);
        edtName = view.findViewById(R.id.edt_name_item_detail);
        edtPrice = view.findViewById(R.id.edt_price_item_detail);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        setData();

        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favorite) {
                    imgFavorite.setImageResource(R.drawable.baseline_favorite_24_white);
                    favorite = !favorite;

                } else {
                    imgFavorite.setImageResource(R.drawable.baseline_favorite_24);
                    favorite = !favorite;
                    sendNotificationFavorite();
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
                Toast.makeText(getContext(), "" + favorite, Toast.LENGTH_SHORT).show();
                editor.putBoolean("favorite", favorite);
                editor.apply();

                sendNotification();
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

    private void sendNotificationFavorite() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picture_notifi);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent resultIntent = new Intent(getContext(), MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(getNotificationId(),
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(getContext(), MyNotification.CHANNEL_ID_2)
                .setContentTitle(TITLE_NOTIFI)
                .setContentText(CONTENT_NOTIFI)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(CONTENT_NOTIFI))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setLargeIcon(bitmap)
                .setSound(uri)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setColor(getResources().getColor(R.color.green))
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(getNotificationId(), notification);

    }

    private int getNotificationId(){
        return (int) new Date().getTime();
    }

    private void sendNotification() {
        // binding service
        Intent intentService = new Intent(getActivity(), NotifiService.class);
        String id = txtId.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String contentNotifi = "update: " + id + ", " + name;
        intentService.putExtra("key_content_intent", contentNotifi);

        getContext().startService(intentService);
    }

    private void selectImageFromGallery(){
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
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