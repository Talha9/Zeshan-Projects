package com.earthmap.satellite.map.location.map.satelliteTrackerModule.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SatelliteMarkersModel implements Parcelable {
    String satelliteName;
    String launchedDate;
    double latitude;
    double longitude;

    public SatelliteMarkersModel(String satelliteName, String launchedDate, double latitude, double longitude) {
        this.satelliteName = satelliteName;
        this.launchedDate = launchedDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected SatelliteMarkersModel(Parcel in) {
        satelliteName = in.readString();
        launchedDate = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(satelliteName);
        dest.writeString(launchedDate);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SatelliteMarkersModel> CREATOR = new Creator<SatelliteMarkersModel>() {
        @Override
        public SatelliteMarkersModel createFromParcel(Parcel in) {
            return new SatelliteMarkersModel(in);
        }

        @Override
        public SatelliteMarkersModel[] newArray(int size) {
            return new SatelliteMarkersModel[size];
        }
    };

    public String getSatelliteName() {
        return satelliteName;
    }

    public void setSatelliteName(String satelliteName) {
        this.satelliteName = satelliteName;
    }

    public String getLaunchedDate() {
        return launchedDate;
    }

    public void setLaunchedDate(String launchedDate) {
        this.launchedDate = launchedDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
