package com.carsguide.model;

import com.google.gson.annotations.SerializedName;

public class Car {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("make_icon")
    private String makeUrl;

    private boolean favorite;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMakeUrl() {
        return makeUrl;
    }

    public void setMakeUrl(String makeUrl) {
        this.makeUrl = makeUrl;
    }
}
