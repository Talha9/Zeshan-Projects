package com.earthmap.satellite.map.location.map.nearBy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NearByModel implements Parcelable {
    String nearByCatName;
    int nearByCatIcon;
    String nearByCatId;

    public NearByModel(String nearByCatName, int nearByCatIcon, String nearByCatId) {
        this.nearByCatName = nearByCatName;
        this.nearByCatIcon = nearByCatIcon;
        this.nearByCatId = nearByCatId;
    }

    protected NearByModel(Parcel in) {
        nearByCatName = in.readString();
        nearByCatIcon = in.readInt();
        nearByCatId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nearByCatName);
        dest.writeInt(nearByCatIcon);
        dest.writeString(nearByCatId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NearByModel> CREATOR = new Creator<NearByModel>() {
        @Override
        public NearByModel createFromParcel(Parcel in) {
            return new NearByModel(in);
        }

        @Override
        public NearByModel[] newArray(int size) {
            return new NearByModel[size];
        }
    };

    public String getNearByCatName() {
        return nearByCatName;
    }

    public void setNearByCatName(String nearByCatName) {
        this.nearByCatName = nearByCatName;
    }

    public int getNearByCatIcon() {
        return nearByCatIcon;
    }

    public void setNearByCatIcon(int nearByCatIcon) {
        this.nearByCatIcon = nearByCatIcon;
    }

    public String getNearByCatId() {
        return nearByCatId;
    }

    public void setNearByCatId(String nearByCatId) {
        this.nearByCatId = nearByCatId;
    }
}
