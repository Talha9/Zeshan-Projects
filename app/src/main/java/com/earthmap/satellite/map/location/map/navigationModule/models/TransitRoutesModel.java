package com.earthmap.satellite.map.location.map.navigationModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TransitRoutesModel implements Parcelable {
    int Id;
    String destinationPoint="Way";
    private String latitude;
    private String longitude;


    protected TransitRoutesModel(Parcel in) {
        Id = in.readInt();
        destinationPoint = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(destinationPoint);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransitRoutesModel> CREATOR = new Creator<TransitRoutesModel>() {
        @Override
        public TransitRoutesModel createFromParcel(Parcel in) {
            return new TransitRoutesModel(in);
        }

        @Override
        public TransitRoutesModel[] newArray(int size) {
            return new TransitRoutesModel[size];
        }
    };

    public String getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public TransitRoutesModel(String destinationPoint, String latitude, String longitude) {
        this.destinationPoint = destinationPoint;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public TransitRoutesModel() {
    }

    public TransitRoutesModel(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public TransitRoutesModel(int id) {
        Id = id;
    }

    public TransitRoutesModel(int id, String destinationPoint) {
        Id = id;
        this.destinationPoint = destinationPoint;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

}
