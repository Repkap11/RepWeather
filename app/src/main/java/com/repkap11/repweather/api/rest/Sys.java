
package com.repkap11.repweather.api.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("type")
    @Expose
    public Integer type;
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
    @SerializedName("country")
    @Expose
    public String country;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("sunrise")
    @Expose
    public Integer sunrise;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("sunset")
    @Expose
    public Integer sunset;

}
