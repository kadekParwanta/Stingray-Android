package com.stingray.stingrayandroid.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kadek_P on 7/12/2016.
 */
public class AccessToken {
    @SerializedName("ttl")
    private Double ttl;
    @SerializedName("created")
    private String created;
    @SerializedName("id")
    private String id;
    @SerializedName("userId")
    private Integer userId;

    public Double getTtl() {
        return ttl;
    }

    public void setTtl(Double ttl) {
        this.ttl = ttl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
