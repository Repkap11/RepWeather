
package com.repkap11.repweather.api.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("coord")
    @Expose
    public Coord coord;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("weather")
    @Expose
    public List<Object> weather = null;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("base")
    @Expose
    public String base;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("main")
    @Expose
    public Main main;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("visibility")
    @Expose
    public Integer visibility;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("wind")
    @Expose
    public Wind wind;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("dt")
    @Expose
    public Integer dt;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("sys")
    @Expose
    public Sys sys;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("timezone")
    @Expose
    public Integer timezone;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("id")
    @Expose
    public Integer id;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("name")
    @Expose
    public String name;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("cod")
    @Expose
    public Integer cod;

}
