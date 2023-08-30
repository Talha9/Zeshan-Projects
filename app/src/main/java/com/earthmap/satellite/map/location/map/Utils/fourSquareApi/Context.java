package com.earthmap.satellite.map.location.map.Utils.fourSquareApi;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context {

    @SerializedName("geo_bounds")
    @Expose
    private GeoBounds geoBounds;

    public GeoBounds getGeoBounds() {
        return geoBounds;
    }

    public void setGeoBounds(GeoBounds geoBounds) {
        this.geoBounds = geoBounds;
    }

}
