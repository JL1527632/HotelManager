package com.leon.example.orangehotel.DataStructures;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class DailyDetails implements Parcelable {

    private RoomInformation mRoomInformation;
    private Reservation mReservation;
    private RoomStatus mRoomStatus;

    final static String LOG_TAG = "DailyDetails";

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

    public DailyDetails(Parcel in) {
        //TODO: How to read a nested Parceable
        mRoomInformation = in.readParcelable(ClassLoader.getSystemClassLoader());
        mReservation = in.readParcelable(ClassLoader.getSystemClassLoader());
        mRoomStatus = in.readParcelable(ClassLoader.getSystemClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mRoomInformation, 0);
        dest.writeParcelable(mReservation, 0);
        dest.writeParcelable(mRoomStatus, 0);
    }

    /**
     * Creates a master ArrayList from a list of rooms, status, and reservations.
     * This is the main method which will be called when retrieving data from Firebase
     * Database.
     */
    public static ArrayList<DailyDetails> createList(ArrayList<RoomInformation> rooms,
                                                     ArrayList<Reservation> reservations,
                                                     ArrayList<RoomStatus> statusList) {

        //the list to be returned.
        ArrayList<DailyDetails> myList = new ArrayList<>();
        ArrayList<Reservation> tempReservationsList = reservations;
        ArrayList<RoomStatus> tempStatusList = statusList;

        /* Sizes must match in order to proceed. total reservations won't cause an error,
         * as it's assumed some rooms won't have reservations and treated as 'available' */
        if (rooms.size() != statusList.size())
            return null;

        int roomTotal = rooms.size();

        //STEP 1: read all data in Rooms List and create DailyDetails with room information only.
        for (int i = 0; i < roomTotal; i++) {
            myList.add(new DailyDetails(rooms.get(i)));
        }

        /* Step 2: Go through list and merge statuses into return list */
        boolean failedAssignment = false;
        boolean notYetAssigned = true;
        RoomStatus tempRoomStatus;

        int r = 0; //iterates through room list
        int listSize = myList.size();

        /* Go through tempStatusList and tries to match to RoomInformation.
        If successful, the information is added to return list.
        the particular index read is removed from tempStatusList */
        while (!failedAssignment && !tempStatusList.isEmpty()) {

            if (r < myList.size())
                r = 0;

            notYetAssigned = true;
            tempRoomStatus = tempStatusList.get(0);

            while (r < listSize && notYetAssigned) {

                if (myList.get(r).getRoomNumber() == tempRoomStatus.getRoomNumber()) {
                    //System.out.println("Status loop. Calling setRS");
                    tempRoomStatus = null;
                    myList.get(r).setRoomStatus(tempStatusList.remove(0));
                    notYetAssigned = false;
                } else {
                    r++;
                }
            } //END OF WHILE LOOP

             /* Checks to see why the while loop ended. If it's for reaching end of list,
            updated failesAssignment. forces outer while loop to close */
            if (r >= listSize) {
                Log.e(LOG_TAG, "failed RoomStatus update");
                failedAssignment = true;
            }

        }

        /* Reached end of list. If tempStatusList still has elements, those
        elements did not match any rooms in roomInformation. List can't be created */
        if (tempStatusList.size() != 0 || failedAssignment) {
            Log.e(LOG_TAG, "Invalid data. No list created");
            return null;
        }

        //STEP 3: Assign reservations to existing list.
        failedAssignment = false;
        Reservation tempReservation;

        int n = 0; //counter for tempReservationsList
        int presentAssignedRoom = 0;
        int reservationsTotal = reservations.size();
        //boolean roomFound = false;

        //Go through complete reservation list and instert values where they belong.
        while (n < reservationsTotal && !failedAssignment && !tempReservationsList.isEmpty()) {

            r = 0;
            notYetAssigned = true;
            presentAssignedRoom = reservations.get(n).getAssignedRoom();

            while (r < roomTotal && notYetAssigned) {
                if (myList.get(r).getRoomNumber() == presentAssignedRoom) {
                    myList.get(r).setReservationData(tempReservationsList.remove(n));
                    notYetAssigned = false;
                } else r++;
            }

            //If we reached end of list in RoomInformation, assignment failed. ends loop.
            if (r > roomTotal) {
                failedAssignment = true;
            }
        }


        if (tempReservationsList.size() != 0 || failedAssignment) {
            Log.e(LOG_TAG, "Unable to assign Reservation data. ");
            return null;
        }

        /* Runs one final verification that all elements in list have roomInformation and roomStatus.
        if any room status are not found, they're created as default DIRTY. This allows for any
         RoomStatus list that contains duplicates to be processed. */

        for (int i = 0; i < myList.size(); i++) {
            if (myList.get(i).mRoomStatus == null) {
                myList.get(i).setRoomStatus(new RoomStatus(myList.get(i).getRoomNumber(), RoomStatus.DIRTY));
            }
        }

        return myList;
    }

    //Private constructor, only to be used by ArrayList creator
    private DailyDetails(RoomInformation roomInformation) {
        mRoomInformation = roomInformation;
        mReservation = null;
        mRoomStatus = null;
    }

    //Public constructor with all Objects.
    public DailyDetails(RoomInformation roomInformation, Reservation reservation, RoomStatus status) {

        mRoomInformation = roomInformation;

        if (roomInformation != null) {
            //run final check with RoomStatus data
            if (setRoomStatus(status)) {
                //Check on RESERVATION
                if (reservation != null) {

                    if (!setReservationData(reservation))
                        mReservation = null;
                } else {
                    mReservation = null;
                }
            }
        }
        //No room information present, unable to create object.
        else {
            mRoomInformation = null;
            mReservation = null;
            mRoomStatus = null;
        }

    }

    //public constructor with no Reservation data
    public DailyDetails(RoomInformation roomInformation, RoomStatus status) {

        mRoomInformation = roomInformation;

        if (roomInformation != null) {
            //run check with RoomStatus data
            if (setRoomStatus(status)) {
                mReservation = null;
            }
            //Status is invalid.
            else {
                mRoomInformation = null;
                mReservation = null;
                mRoomStatus = null;
                Log.e(LOG_TAG, "Unable to create. Room information mismatch");
            }
        }
        //No room information present, unable to create object.
        else {
            mRoomInformation = null;
            mReservation = null;
            mRoomStatus = null;
            Log.e(LOG_TAG, " Unable to create. No basic room information found");
        }

    }

    /* Assigns a Reservation object to an existing DailyDetails object.
     * returns true if reservation could be added. false if the data
     * does not match to existing room details' room number assignment.
     */
    public boolean setReservationData(Reservation reservation) {

        //Verify both objects have been initiated
        if (mRoomInformation != null && reservation != null) {
            //Check for match
            if (mRoomInformation.getRoomNumber() == reservation.getAssignedRoom()) {
                this.mReservation = reservation;
                return true;
            }
            //if no match, can be created if the Reservation has no room assigned.
            else if (!reservation.hasAssignedRoom()) {
                this.mReservation = reservation;
                mReservation.assignRoom(mRoomInformation.getRoomNumber());

                return true;
            } else return false;
        } else return false;
    }

    /* Assigns a RoomStatus object to an existing DailyDetails object.
     * returns true if RoomStatus could be added. false if the data
     * does not match to existing room details' room number assignment.
     */
    public boolean setRoomStatus(RoomStatus status) {
        //System.out.println("set Room Status called");
        if (mRoomInformation != null && status != null) {
            if (mRoomInformation.getRoomNumber() == status.getRoomNumber()) {
                //System.out.println("setting status");
                this.mRoomStatus = status;
                return true;
            } else return false;
        } else return false;
    }

    public int getRoomNumber() {
        return mRoomInformation.getRoomNumber();
    }

    public RoomInformation getRoomInformation() {
        return mRoomInformation;
    }

    public String getRoomAsText() {
        return "" + mRoomInformation.getRoomNumber();
    }

    public int getRoomType() {
        return mRoomInformation.getRoomType();
    }

    //Returns the number value of the Room Status
    public int getRoomStatus() {
        return mRoomStatus.getRoomStatus();
    }

    //Return RoomStatus object
    public RoomStatus getRoomStatusObject() {
        return mRoomStatus;
    }

    //Checks to see if a reservation is present.
    public boolean hasReservation() {
        return (mReservation != null);
    }

    public Reservation getReservationDetails() {
        return mReservation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
