
package com.repkap11.repweather.api.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("speed")
    @Expose
    public Double speed;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("deg")
    @Expose
    public Integer deg;

}
