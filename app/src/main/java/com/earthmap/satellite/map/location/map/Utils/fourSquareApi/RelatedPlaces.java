package com.earthmap.satellite.map.location.map.Utils.fourSquareApi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelatedPlaces {

    @SerializedName("children")
    @Expose
    private List<Child> children = null;
    @SerializedName("parent")
    @Expose
    private Parent parent;

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

}
