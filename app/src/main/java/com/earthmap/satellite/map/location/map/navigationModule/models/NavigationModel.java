package com.earthmap.satellite.map.location.map.navigationModule.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NavigationModel implements Parcelable {

    String navTxt;
    int navImg;
    int currentPosition;

    public NavigationModel(String navTxt, int navImg, int currentPosition) {
        this.navTxt = navTxt;
        this.navImg = navImg;
        this.currentPosition = currentPosition;
    }

    protected NavigationModel(Parcel in) {
        navTxt = in.readString();
        navImg = in.readInt();
        currentPosition = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(navTxt);
        dest.writeInt(navImg);
        dest.writeInt(currentPosition);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NavigationModel> CREATOR = new Creator<NavigationModel>() {
        @Override
        public NavigationModel createFromParcel(Parcel in) {
            return new NavigationModel(in);
        }

        @Override
        public NavigationModel[] newArray(int size) {
            return new NavigationModel[size];
        }
    };

    public String getNavTxt() {
        return navTxt;
    }

    public void setNavTxt(String navTxt) {
        this.navTxt = navTxt;
    }

    public int getNavImg() {
        return navImg;
    }

    public void setNavImg(int navImg) {
        this.navImg = navImg;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
