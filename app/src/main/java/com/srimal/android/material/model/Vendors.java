package com.srimal.android.material.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by srimal on 5/7/2016.
 */
public class Vendors implements Serializable{

    @SerializedName("Name")
    private String Name;

    @SerializedName("Contact_data")
    private String Contact_data;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContact_data() {
        return Contact_data;
    }

    public void setContact_data(String contact_data) {
        Contact_data = contact_data;
    }
}
