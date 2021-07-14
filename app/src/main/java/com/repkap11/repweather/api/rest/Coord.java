
package com.repkap11.repweather.api.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("lon")
    @Expose
    public Double lon;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("lat")
    @Expose
    public Double lat;

}
