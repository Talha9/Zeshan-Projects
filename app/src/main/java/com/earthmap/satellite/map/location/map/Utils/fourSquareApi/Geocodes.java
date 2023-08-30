package com.earthmap.satellite.map.location.map.Utils.fourSquareApi;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Geocodes {

    @SerializedName("main")
    @Expose
    private Main main;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

}
