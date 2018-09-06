package com.leon.example.orangehotel.DataStructures;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.leon.example.orangehotel.R;
import com.leon.example.orangehotel.SupportUtilities.DateUtilities;

import java.util.Date;
import java.util.HashMap;

public class Reservation implements Parcelable {

    private final int NO_ROOM = -1;

    private String name;
    private double price;
    private Date checkInDate;
    private Date checkOutDate;
    private int assignedRoom;

    public Reservation() {
        //empty constructor. Required for DataSnapshot.getValue operations.
    }

    public Reservation(String name, double price, Date in, Date out) {
        this.name = name;
        this.price = price;
        checkInDate = in;
        checkOutDate = out;
        assignedRoom = NO_ROOM;
    }

    public Reservation(String name, double price, Date in, Date out, int room) {
        this.name = name;
        this.price = price;
        checkInDate = in;
        checkOutDate = out;
        assignedRoom = room;
    }

    protected Reservation(Parcel in) {
        name = in.readString();
        price = in.readDouble();

        int dIn = in.readInt();
        int mIn = in.readInt();
        int yIn = in.readInt();

        int dOut = in.readInt();
        int mOut = in.readInt();
        int yOut = in.readInt();

        assignedRoom = in.readInt();
        checkInDate = DateUtilities.getDateObject(yIn, mIn, dIn);
        checkOutDate = DateUtilities.getDateObject(yOut, mOut, dOut);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);

        //checkInDateData
        dest.writeInt(DateUtilities.getDay(checkInDate));
        dest.writeInt(DateUtilities.getMonth(checkInDate));
        dest.writeInt(DateUtilities.getYear(checkInDate));

        //checkOutDateData
        dest.writeInt(DateUtilities.getDay(checkOutDate));
        dest.writeInt(DateUtilities.getMonth(checkOutDate));
        dest.writeInt(DateUtilities.getYear(checkOutDate));

        dest.writeInt(assignedRoom);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    public void assignRoom(int room) {
        assignedRoom = room;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public int getAssignedRoom() {
        return assignedRoom;
    }

    public boolean hasAssignedRoom() {
        return !(assignedRoom == NO_ROOM);
    }

    /* Verifies that data information is valid for any given reservation.
    Name and price must be present. Price cannot be negative, but is allowed to be a zero.
    Check-out date must be after check-in date. It is not allowed to be the same date.
    */
    public static boolean verifyData(Reservation reservation) {
        if (reservation.getName().isEmpty())
            return false;

        if (reservation.getPrice() < 0.00)
            return false;

        //If the check-in date is after or matches the check-out date, information is invalid.
        if (reservation.getCheckInDate().compareTo(reservation.getCheckOutDate()) >= 0) {
            return false;
        }

        return true;
    }

    public String toString(Context context) {
        String value = name + ". \n\n" + context.getString(R.string.arrival_date) + " "
                + DateUtilities.getDateFormat(checkInDate) + " " +
                context.getString(R.string.with) + " "
                + context.getString(R.string.departure_date) + ": " + DateUtilities.getDateFormat(checkOutDate);

        if (assignedRoom == NO_ROOM)
            value = value + " " + context.getString(R.string.with_no_assignment);
        else
            value = value + " " + context.getString(R.string.assigned_to) + " " + assignedRoom + " ";

        value = value + context.getString(R.string.at_price) + " $" + price + " "
                + context.getString(R.string.per_night);

        return value;
    }

    public String toLog() {
        return name + "rr" + assignedRoom;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("price", price);
        result.put("datein_y", DateUtilities.getYear(checkInDate));
        result.put("datein_m", DateUtilities.getMonth(checkInDate));
        result.put("datein_d", DateUtilities.getDay(checkInDate));
        result.put("dateout_y", DateUtilities.getYear(checkOutDate));
        result.put("dateout_m", DateUtilities.getMonth(checkOutDate));
        result.put("dateout_d", DateUtilities.getDay(checkOutDate));
        result.put("assignedRoom", assignedRoom);

        return result;
    }

}
