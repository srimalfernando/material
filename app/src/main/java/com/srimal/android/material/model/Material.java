package com.srimal.android.material.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by srimal on 5/7/2016.
 */
public class Material implements Serializable {
    @SerializedName("Material_id")
    private String Material_id;

    @SerializedName("Material_short_name")
    private String Material_short_name;

    @SerializedName("Material_description")
    private String Material_description;

    @SerializedName("Type")
    private String Type;

    @SerializedName("Property")
    private String Property;

    @SerializedName("Value1")
    private String Value1;

    @SerializedName("Value2")
    private String Value2;

    @SerializedName("Name")
    private String Name;

    public String getMaterial_id() {
        return Material_id;
    }

    public void setMaterial_id(String material_id) {
        Material_id = material_id;
    }

    public String getMaterial_short_name() {
        return Material_short_name;
    }

    public void setMaterial_short_name(String material_short_name) {
        Material_short_name = material_short_name;
    }

    public String getMaterial_description() {
        return Material_description;
    }

    public void setMaterial_description(String material_description) {
        Material_description = material_description;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getProperty() {
        return Property;
    }

    public void setProperty(String property) {
        Property = property;
    }

    public String getValue1() {
        return Value1;
    }

    public void setValue1(String value1) {
        Value1 = value1;
    }

    public String getValue2() {
        return Value2;
    }

    public void setValue2(String value2) {
        Value2 = value2;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
