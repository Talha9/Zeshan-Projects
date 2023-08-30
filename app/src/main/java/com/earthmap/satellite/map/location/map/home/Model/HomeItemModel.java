package com.earthmap.satellite.map.location.map.home.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeItemModel implements Parcelable {
    int src;
    int itemCircleColour;
    String itemTxt;

    public HomeItemModel(int src, int itemCircleColour, String itemTxt) {
        this.src = src;
        this.itemCircleColour = itemCircleColour;
        this.itemTxt = itemTxt;
    }

    protected HomeItemModel(Parcel in) {
        src = in.readInt();
        itemCircleColour = in.readInt();
        itemTxt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(src);
        dest.writeInt(itemCircleColour);
        dest.writeString(itemTxt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HomeItemModel> CREATOR = new Creator<HomeItemModel>() {
        @Override
        public HomeItemModel createFromParcel(Parcel in) {
            return new HomeItemModel(in);
        }

        @Override
        public HomeItemModel[] newArray(int size) {
            return new HomeItemModel[size];
        }
    };

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getItemCircleColour() {
        return itemCircleColour;
    }

    public void setItemCircleColour(int itemCircleColour) {
        this.itemCircleColour = itemCircleColour;
    }

    public String getItemTxt() {
        return itemTxt;
    }

    public void setItemTxt(String itemTxt) {
        this.itemTxt = itemTxt;
    }
}
