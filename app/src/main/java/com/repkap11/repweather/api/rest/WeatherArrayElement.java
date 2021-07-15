package com.repkap11.repweather.api.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherArrayElement {

    /**
     * (Required)
     */
    @SerializedName("id")
    @Expose
    public Integer id;
    /**
     * (Required)
     */
    @SerializedName("main")
    @Expose
    public String main;
    /**
     * (Required)
     */
    @SerializedName("description")
    @Expose
    public String description;
    /**
     * (Required)
     */
    @SerializedName("icon")
    @Expose
    public String icon;
    /**
     * (Required)
     */
    @SerializedName("wind")
    @Expose
    public Wind wind;
    /**
     * (Required)
     */
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    /**
     * (Required)
     */
    @SerializedName("dt")
    @Expose
    public Integer dt;
    /**
     * (Required)
     */
    @SerializedName("sys")
    @Expose
    public Sys sys;
    /**
     * (Required)
     */
    @SerializedName("timezone")
    @Expose
    public Integer timezone;
    /**
     * (Required)
     */
    @SerializedName("name")
    @Expose
    public String name;
    /**
     * (Required)
     */
    @SerializedName("cod")
    @Expose
    public Integer cod;

    public String zipcode;
}
