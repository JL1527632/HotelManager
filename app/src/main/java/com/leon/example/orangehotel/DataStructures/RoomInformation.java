package com.leon.example.orangehotel.DataStructures;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.leon.example.orangehotel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomInformation implements Parcelable {

    public final int SINGLE_BED = 100;
    public final int DOUBLE_BED = 200;
    public final int SINGLE_SMOKING = 300;
    public final int DOUBLE_SMOKING = 400;
    public final int SUITE = 500;
    public final int SUITE_SMOKING = 600;

    private int roomNumber;
    private int roomType;

    public RoomInformation() {
        //Empty constructor. Required by DataSnapshot operations on Firebase Realtime Database
    }

    //Constructor for only room type and number. defaults to CLEAN status.
    public RoomInformation(int number, int type) {
        roomNumber = number;
        roomType = type;
    }

    public static final Creator<RoomInformation> CREATOR = new Creator<RoomInformation>() {
        @Override
        public RoomInformation createFromParcel(Parcel in) {
            return new RoomInformation(in);
        }

        @Override
        public RoomInformation[] newArray(int size) {
            return new RoomInformation[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(roomNumber);
        dest.writeInt(roomType);
    }

    public RoomInformation(Parcel in) {
        roomNumber = in.readInt();
        roomType = in.readInt();
    }

    //Getter methods for variables.
    public int getRoomType() {
        return roomType;
    }


    public int getRoomNumber() {
        return roomNumber;
    }

    public String toString(Context context) {
        String message;
        message = context.getString(R.string.room_number) + " " + roomNumber + ". \n" + context.getString(R.string.room_type)
                + ": " + RoomTypes.typeAsString(context, roomType);
        return message;
    }

    public static ArrayList<RoomInformation> createRoomList(int[] roomCounts) {
        ArrayList<RoomInformation> myList = new ArrayList<>();

        for (int i = 0; i < roomCounts.length; i++) {
            for (int k = 0; k < roomCounts[i]; k++) {
                myList.add(new RoomInformation(RoomTypes.TYPE_ARRAY[i] + k, RoomTypes.TYPE_ARRAY[i]));
            }
        }

        return myList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomNumber", roomNumber);
        result.put("roomType", roomType);

        return result;
    }

}