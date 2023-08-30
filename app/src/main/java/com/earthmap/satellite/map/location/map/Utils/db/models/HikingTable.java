package com.earthmap.satellite.map.location.map.Utils.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.earthmap.satellite.map.location.map.Utils.db.LatlngTypeConverter;

import java.util.List;

@Entity
public class HikingTable implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id;

    @ColumnInfo(name = "activityName")
    public String activityName;

    @ColumnInfo(name = "totalDuration")
    public String totalDuration;

    @ColumnInfo(name = "totalDistance")
    public double totalDistance;

    @ColumnInfo(name = "currentDate")
    public String currentDate;

    @TypeConverters(LatlngTypeConverter.class)
    public List<MyLatLng> listLatlng;

    protected HikingTable(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        activityName = in.readString();
        totalDuration = in.readString();
        totalDistance = in.readDouble();
        currentDate = in.readString();
        listLatlng = in.createTypedArrayList(MyLatLng.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(activityName);
        dest.writeString(totalDuration);
        dest.writeDouble(totalDistance);
        dest.writeString(currentDate);
        dest.writeTypedList(listLatlng);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HikingTable> CREATOR = new Creator<HikingTable>() {
        @Override
        public HikingTable createFromParcel(Parcel in) {
            return new HikingTable(in);
        }

        @Override
        public HikingTable[] newArray(int size) {
            return new HikingTable[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public List<MyLatLng> getListLatlng() {
        return listLatlng;
    }

    public void setListLatlng(List<MyLatLng> listLatlng) {
        this.listLatlng = listLatlng;
    }

    public HikingTable(String activityName, String totalDuration, double totalDistance, String currentDate, List<MyLatLng> listLatlng) {
        this.activityName = activityName;
        this.totalDuration = totalDuration;
        this.totalDistance = totalDistance;
        this.currentDate = currentDate;
        this.listLatlng = listLatlng;
    }
}
