package com.weihan.ligong.entities;

import com.google.gson.annotations.SerializedName;

public class UserLogin {
    @SerializedName("odata.etag")
    private String odata_etag;
    private String User_ID, Name, Password, ETag;

    public String getOdata_etag() {
        return odata_etag;
    }

    public void setOdata_etag(String odata_etag) {
        this.odata_etag = odata_etag;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getETag() {
        return ETag;
    }

    public void setETag(String ETag) {
        this.ETag = ETag;
    }
}
