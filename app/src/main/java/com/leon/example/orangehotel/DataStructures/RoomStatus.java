package com.leon.example.orangehotel.DataStructures;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.leon.example.orangehotel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomStatus implements Parcelable {

    //Static values for room stages in a hotel setting.
    public static final int CLEAN = 100;
    public static final int DIRTY = 200;
    public static final int OCCUPIED = 300;

    private int roomNumber;
    private int roomStatus;

    public RoomStatus() {
        //Empty constructor. Required by Firebase Realtime Database DataSnapshop operations.
    }

    public RoomStatus(int roomNumber, int status) {
        this.roomNumber = roomNumber;
        this.roomStatus = status;
    }

    public RoomStatus(Parcel in) {
        roomNumber = in.readInt();
        roomStatus = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(roomNumber);
        dest.writeInt(roomStatus);
    }

    public int getRoomStatus() {
        return roomStatus;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    //public static String getStatusAsString(Context context, int status
    public static String getStatusAsString(Context context, int status) {
        String statusString = "";
        switch (status) {
            case CLEAN:
                statusString = context.getString(R.string.status_clean);
                break;
            case OCCUPIED:
                statusString = context.getString(R.string.status_occupied);
                break;
            case DIRTY:
            default:
                statusString = context.getString(R.string.status_dirty);
        }

        return statusString;
    }

    public static final Creator<RoomStatus> CREATOR = new Creator<RoomStatus>() {
        @Override
        public RoomStatus createFromParcel(Parcel in) {
            return new RoomStatus(in);
        }

        @Override
        public RoomStatus[] newArray(int size) {
            return new RoomStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Map<String, Object> toMap() {

        Map<String, Object> result = new HashMap<>();
        result.put("roomNumber", roomNumber);
        result.put("roomStatus", roomStatus);

        return result;
    }

    public static ArrayList<RoomStatus> generateListFromIndexes(ArrayList<Integer> indeces, int fixedStatus)
    {
        ArrayList<RoomStatus> myStatusList = new ArrayList<>();

        for(int i =0; i < indeces.size(); i++)
        {
            myStatusList.add( new RoomStatus(indeces.get(i), fixedStatus));
        }

        return myStatusList;
    }
}
