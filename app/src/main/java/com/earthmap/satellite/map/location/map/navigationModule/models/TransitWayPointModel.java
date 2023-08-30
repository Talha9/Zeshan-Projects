package com.earthmap.satellite.map.location.map.navigationModule.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TransitWayPointModel implements Parcelable {
    Double currentLat=0.0;
    Double currentLng=0.0;
    Double destinationLat=0.0;
    Double destinationLng=0.0;
     ArrayList<TransitRoutesModel> list;

    public TransitWayPointModel(Double currentLat, Double currentLng, Double destinationLat, Double destinationLng, ArrayList<TransitRoutesModel> list) {
        this.currentLat = currentLat;
        this.currentLng = currentLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
        this.list = list;
    }

    protected TransitWayPointModel(Parcel in) {
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
        list = in.createTypedArrayList(TransitRoutesModel.CREATOR);
    }

    public static final Creator<TransitWayPointModel> CREATOR = new Creator<TransitWayPointModel>() {
        @Override
        public TransitWayPointModel createFromParcel(Parcel in) {
            return new TransitWayPointModel(in);
        }

        @Override
        public TransitWayPointModel[] newArray(int size) {
            return new TransitWayPointModel[size];
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

    public ArrayList<TransitRoutesModel> getList() {
        return list;
    }

    public void setList(ArrayList<TransitRoutesModel> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (currentLat == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(currentLat);
        }
        if (currentLng == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(currentLng);
        }
        if (destinationLat == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(destinationLat);
        }
        if (destinationLng == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(destinationLng);
        }
        parcel.writeTypedList(list);
    }
}
