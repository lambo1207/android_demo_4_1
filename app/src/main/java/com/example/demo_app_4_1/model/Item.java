package com.example.demo_app_4_1.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int key;
    private int id;
    private String resourceImage;
    private String name;
    private int price;
    private boolean favorite;

    public Item(String resourceImage, int id, String name, int price, boolean favorite) {
        this.resourceImage = resourceImage;
        this.id = id;
        this.name = name;
        this.price = price;
        this.favorite = favorite;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(String resourceImage) {
        this.resourceImage = resourceImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
