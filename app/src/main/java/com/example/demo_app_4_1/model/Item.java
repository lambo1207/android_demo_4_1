package com.example.demo_app_4_1.model;

public class Item {

    private String resourceImage;
    private int id;
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
