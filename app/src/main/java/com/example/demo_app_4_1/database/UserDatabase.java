package com.example.demo_app_4_1.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.demo_app_4_1.model.Item;

@Database(entities = Item.class, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    private static final String NAME_DATABASE = "item.db";
    private static UserDatabase instance;

    public static synchronized UserDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UserDatabase.class, NAME_DATABASE)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract UserDAO userDAO();
}
