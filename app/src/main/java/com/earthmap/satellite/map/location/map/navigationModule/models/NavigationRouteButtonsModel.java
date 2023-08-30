package com.earthmap.satellite.map.location.map.navigationModule.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mapbox.api.directions.v5.models.DirectionsRoute;

public class NavigationRouteButtonsModel implements Parcelable {
    String distanceTxt;
    String routeIndex;
    DirectionsRoute route;
    int currentPosition;


    protected NavigationRouteButtonsModel(Parcel in) {
        distanceTxt = in.readString();
        routeIndex = in.readString();
        currentPosition = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(distanceTxt);
        dest.writeString(routeIndex);
        dest.writeInt(currentPosition);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NavigationRouteButtonsModel> CREATOR = new Creator<NavigationRouteButtonsModel>() {
        @Override
        public NavigationRouteButtonsModel createFromParcel(Parcel in) {
            return new NavigationRouteButtonsModel(in);
        }

        @Override
        public NavigationRouteButtonsModel[] newArray(int size) {
            return new NavigationRouteButtonsModel[size];
        }
    };

    public String getDistanceTxt() {
        return distanceTxt;
    }

    public void setDistanceTxt(String distanceTxt) {
        this.distanceTxt = distanceTxt;
    }

    public String getRouteIndex() {
        return routeIndex;
    }

    public void setRouteIndex(String routeIndex) {
        this.routeIndex = routeIndex;
    }

    public DirectionsRoute getRoute() {
        return route;
    }

    public void setRoute(DirectionsRoute route) {
        this.route = route;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public NavigationRouteButtonsModel(String distanceTxt, String routeIndex, DirectionsRoute route, int currentPosition) {
        this.distanceTxt = distanceTxt;
        this.routeIndex = routeIndex;
        this.route = route;
        this.currentPosition = currentPosition;
    }
}
