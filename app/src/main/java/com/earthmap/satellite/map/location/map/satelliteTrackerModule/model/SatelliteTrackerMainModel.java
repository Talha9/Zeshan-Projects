package com.earthmap.satellite.map.location.map.satelliteTrackerModule.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SatelliteTrackerMainModel implements Parcelable {
    String satelliteCategoryName;
    int id;

    public SatelliteTrackerMainModel(String satelliteCategoryName, int id) {
        this.satelliteCategoryName = satelliteCategoryName;
        this.id = id;
    }

    protected SatelliteTrackerMainModel(Parcel in) {
        satelliteCategoryName = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(satelliteCategoryName);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SatelliteTrackerMainModel> CREATOR = new Creator<SatelliteTrackerMainModel>() {
        @Override
        public SatelliteTrackerMainModel createFromParcel(Parcel in) {
            return new SatelliteTrackerMainModel(in);
        }

        @Override
        public SatelliteTrackerMainModel[] newArray(int size) {
            return new SatelliteTrackerMainModel[size];
        }
    };

    public String getSatelliteCategoryName() {
        return satelliteCategoryName;
    }

    public void setSatelliteCategoryName(String satelliteCategoryName) {
        this.satelliteCategoryName = satelliteCategoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
