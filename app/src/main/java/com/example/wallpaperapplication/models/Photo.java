package com.example.wallpaperapplication.models;

import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("id")
    private String id;
    @SerializedName("description")
    private String description;
    @SerializedName("urls")
    private PhotoURL url =new PhotoURL();
    @SerializedName("user")
    private User user = new User();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PhotoURL getUrl() {
        return url;
    }

    public void setUrl(PhotoURL url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
