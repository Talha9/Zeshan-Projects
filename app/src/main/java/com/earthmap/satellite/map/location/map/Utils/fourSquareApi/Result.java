package com.earthmap.satellite.map.location.map.Utils.fourSquareApi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("fsq_id")
    @Expose
    private String fsqId;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("chains")
    @Expose
    private List<Object> chains = null;
    @SerializedName("distance")
    @Expose
    private Integer distance;
    @SerializedName("geocodes")
    @Expose
    private Geocodes geocodes;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("related_places")
    @Expose
    private RelatedPlaces relatedPlaces;
    @SerializedName("timezone")
    @Expose
    private String timezone;

    public String getFsqId() {
        return fsqId;
    }

    public void setFsqId(String fsqId) {
        this.fsqId = fsqId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Object> getChains() {
        return chains;
    }

    public void setChains(List<Object> chains) {
        this.chains = chains;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Geocodes getGeocodes() {
        return geocodes;
    }

    public void setGeocodes(Geocodes geocodes) {
        this.geocodes = geocodes;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RelatedPlaces getRelatedPlaces() {
        return relatedPlaces;
    }

    public void setRelatedPlaces(RelatedPlaces relatedPlaces) {
        this.relatedPlaces = relatedPlaces;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

}
