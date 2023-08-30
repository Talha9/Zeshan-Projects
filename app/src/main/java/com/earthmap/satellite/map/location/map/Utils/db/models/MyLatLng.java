package com.earthmap.satellite.map.location.map.Utils.db.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MyLatLng implements Parcelable {
    String lats;
    String lngs;

    public MyLatLng(String lats, String lngs) {
        this.lats = lats;
        this.lngs = lngs;
    }

    protected MyLatLng(Parcel in) {
        lats = in.readString();
        lngs = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lats);
        dest.writeString(lngs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyLatLng> CREATOR = new Creator<MyLatLng>() {
        @Override
        public MyLatLng createFromParcel(Parcel in) {
            return new MyLatLng(in);
        }

        @Override
        public MyLatLng[] newArray(int size) {
            return new MyLatLng[size];
        }
    };

    public String getLats() {
        return lats;
    }

    public void setLats(String lats) {
        this.lats = lats;
    }

    public String getLngs() {
        return lngs;
    }

    public void setLngs(String lngs) {
        this.lngs = lngs;
    }
}
