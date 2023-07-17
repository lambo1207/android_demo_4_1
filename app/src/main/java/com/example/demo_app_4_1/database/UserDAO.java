package com.example.demo_app_4_1.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo_app_4_1.model.Item;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void userInsert(Item item);

    @Query("Select * from item")
    List<Item> getListItem();

    @Delete
    void delete(Item item);

    @Update
    void  updateItem(Item item);
}
