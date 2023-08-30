package com.earthmap.satellite.map.location.map.Utils.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavouritesTable implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id;

    @ColumnInfo(name = "streetViewName")
    public String streetViewName;

    @ColumnInfo(name = "streetViewLink")
    public String streetViewLink;

    @ColumnInfo(name = "streetViewFav")
    public boolean streetViewFav;

    protected FavouritesTable(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        streetViewName = in.readString();
        streetViewLink = in.readString();
        streetViewFav = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(streetViewName);
        dest.writeString(streetViewLink);
        dest.writeByte((byte) (streetViewFav ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FavouritesTable> CREATOR = new Creator<FavouritesTable>() {
        @Override
        public FavouritesTable createFromParcel(Parcel in) {
            return new FavouritesTable(in);
        }

        @Override
        public FavouritesTable[] newArray(int size) {
            return new FavouritesTable[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreetViewName() {
        return streetViewName;
    }

    public void setStreetViewName(String streetViewName) {
        this.streetViewName = streetViewName;
    }

    public String getStreetViewLink() {
        return streetViewLink;
    }

    public void setStreetViewLink(String streetViewLink) {
        this.streetViewLink = streetViewLink;
    }

    public boolean isStreetViewFav() {
        return streetViewFav;
    }

    public void setStreetViewFav(boolean streetViewFav) {
        this.streetViewFav = streetViewFav;
    }

    public FavouritesTable(String streetViewName, String streetViewLink, boolean streetViewFav) {
        this.streetViewName = streetViewName;
        this.streetViewLink = streetViewLink;
        this.streetViewFav = streetViewFav;
    }
}
