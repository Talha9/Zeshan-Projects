package com.earthmap.satellite.map.location.map.navigationModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaceResultModel implements Parcelable {
    Double currentLat=0.0;
    Double currentLng=0.0;
    Double destinationLat=0.0;
    Double destinationLng=0.0;

    public PlaceResultModel(Double currentLat, Double currentLng, Double destinationLat, Double destinationLng) {
        this.currentLat = currentLat;
        this.currentLng = currentLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
    }

    public PlaceResultModel() {
    }

    protected PlaceResultModel(Parcel in) {
        if (in.readByte() == 0) {
            currentLat = null;
        } else {
            currentLat = in.readDouble();
        }
        if (in.readByte() == 0) {
            currentLng = null;
        } else {
            currentLng = in.readDouble();
        }
        if (in.readByte() == 0) {
            destinationLat = null;
        } else {
            destinationLat = in.readDouble();
        }
        if (in.readByte() == 0) {
            destinationLng = null;
        } else {
            destinationLng = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (currentLat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(currentLat);
        }
        if (currentLng == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(currentLng);
        }
        if (destinationLat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(destinationLat);
        }
        if (destinationLng == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(destinationLng);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlaceResultModel> CREATOR = new Creator<PlaceResultModel>() {
        @Override
        public PlaceResultModel createFromParcel(Parcel in) {
            return new PlaceResultModel(in);
        }

        @Override
        public PlaceResultModel[] newArray(int size) {
            return new PlaceResultModel[size];
        }
    };

    public Double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(Double currentLat) {
        this.currentLat = currentLat;
    }

    public Double getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(Double currentLng) {
        this.currentLng = currentLng;
    }

    public Double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(Double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public Double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(Double destinationLng) {
        this.destinationLng = destinationLng;
    }
}
