package com.earthmap.satellite.map.location.map.Utils.fourSquareApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoBounds {

    @SerializedName("circle")
    @Expose
    private Circle circle;

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

}
