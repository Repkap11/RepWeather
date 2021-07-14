
package com.repkap11.repweather.api.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("temp")
    @Expose
    public Double temp;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("feels_like")
    @Expose
    public Double feelsLike;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("temp_min")
    @Expose
    public Double tempMin;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("temp_max")
    @Expose
    public Double tempMax;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("pressure")
    @Expose
    public Integer pressure;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("humidity")
    @Expose
    public Integer humidity;

}
