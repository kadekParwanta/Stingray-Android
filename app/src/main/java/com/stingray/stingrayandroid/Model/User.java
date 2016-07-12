package com.stingray.stingrayandroid.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kadek_P on 7/12/2016.
 */
public class User {
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("id")
    private Integer id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
