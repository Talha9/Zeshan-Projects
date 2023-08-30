package com.earthmap.satellite.map.location.map.Utils.fourSquareApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Circle {

    @SerializedName("center")
    @Expose
    private Center center;
    @SerializedName("radius")
    @Expose
    private Integer radius;

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

}
