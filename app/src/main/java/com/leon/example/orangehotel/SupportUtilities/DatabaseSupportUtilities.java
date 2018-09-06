package com.leon.example.orangehotel.SupportUtilities;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.leon.example.orangehotel.DataStructures.Reservation;
import com.leon.example.orangehotel.DataStructures.RoomInformation;
import com.leon.example.orangehotel.DataStructures.RoomStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseSupportUtilities {

    private final static String TAG = "DBSU";

    //Database headers - first-level children/nodes.
    private final static String TABLE_HEADER = "tableHeader";
    private final static String ROOMS_HEADER = "hotelRooms";
    private final static String RESERVATIONS_HEADER = "dailyReservations";
    private final static String STATUS_HEADER = "dailyStatus";
    private final static String UNAVAILABLE_HEADER = "unavailable";
    private final static String SUMMARY_HEADER = "summary";


    public final static int HOTEL_NAME = 0;
    public final static int SIMPLE_SUMMARY = 731;
    public final static int COMPLETE_SUMMARY = 733;

    private final static String IN_DAY = "datein_d";
    private final static String IN_MONTH = "datein_m";
    private final static String IN_YEAR = "datein_y";
    private final static String OUT_DAY = "dateout_d";
    private final static String OUT_MONTH = "dateout_m";
    private final static String OUT_YEAR = "dateout_y";

    private final static String NAME_KEY = "name";
    private final static String PRICE_KEY = "price";
    private final static String ROOM_KEY = "assignedRoom";

    private final static String SINGLE_BED_KEY = "SINGLE_BED";
    private final static String DOUBLE_BED_KEY = "DOUBLE_BED";
    private final static String SINGLE_SMOKING_KEY = "SINGLE_SMOKING";
    private final static String DOUBLE_SMOKING_KEY = "DOUBLE_SMOKING";
    private final static String SUITE_KEY = "SUITE";
    private final static String SUITE_SMOKING_KEY = "SUITE_SMOKING";
    private final static String NAME_HEADER = "hotelName";
    private final static String TOTAL_KEY = "TOTAL_ROOMS";

    //Overrides all information in Database
    public static void overrideAllData(DatabaseReference myRef) {
        myRef.setValue("DATABASE EMPTY");
    }

    //Erases all data in database, only sets up basic header information.
    public static void restoreDatabase(DatabaseReference myRef) {

        //Delete Hotel Room Database
        String databaseChild = "/" + ROOMS_HEADER + "/";
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(databaseChild, Data.getTableHeader());
        myRef.updateChildren(childUpdates);

        //Delete Reservations
        databaseChild = "/" + RESERVATIONS_HEADER + "/";
        childUpdates = new HashMap<>();
        childUpdates.put(databaseChild, Data.getTableHeader());
        myRef.updateChildren(childUpdates);

        //Delete status information
        databaseChild = "/" + STATUS_HEADER + "/";
        childUpdates = new HashMap<>();
        childUpdates.put(databaseChild, Data.getTableHeader());
        myRef.updateChildren(childUpdates);

        //Delete blocked out dates data
        databaseChild = "/" + UNAVAILABLE_HEADER + "/";
        childUpdates = new HashMap<>();
        childUpdates.put(databaseChild, Data.getTableHeader());
        myRef.updateChildren(childUpdates);

        databaseChild = "/" + SUMMARY_HEADER + "/";
        childUpdates = new HashMap<>();
        childUpdates.put(databaseChild, Data.getSummaryTableHeader());
        myRef.updateChildren(childUpdates);
    }

    //Reads RoomInformation values saved to database. Returns arraylist of parsed values.
    public static ArrayList<RoomInformation> readInformationData(DataSnapshot snapshot) {
        DataSnapshot informationNode = snapshot.child(ROOMS_HEADER);
        ArrayList<RoomInformation> hotelRooms = new ArrayList<>();

        for (DataSnapshot child : informationNode.getChildren()) {

            //Log.v("CHILD", child.getKey());
            //PARSE data as a roomInformation object.

            if (!child.getKey().equals(TABLE_HEADER))
                hotelRooms.add(child.getValue(RoomInformation.class));
        }

        //Log.v(TAG, "returning room information list");
        return hotelRooms;
    }

    //Adds a single value of RoomInformation to database.
    public static void updateRoomInformation(DatabaseReference myRef, RoomInformation room) {
        String childNode = "";
        int roomNumber;

        roomNumber = room.getRoomNumber();
        childNode = "/hotelRooms/" + roomNumber;

        Map<String, Object> values = new HashMap<>();
        values.put(childNode, room.toMap());
        myRef.updateChildren(values);
    }

    /* Adds a full list of rooms to hotelRooms in database. Creates new values when not found,
     overrides values when needed. */
    public static void updateRoomInformation(DatabaseReference myRef, ArrayList<RoomInformation> rooms) {

        String childNode = "";
        int roomNumber;

        Map<String, Object> values = new HashMap<>();

        for (int n = 0; n < rooms.size(); n++) {
            roomNumber = rooms.get(n).getRoomNumber();
            childNode = "/hotelRooms/" + roomNumber;

            values.put(childNode, rooms.get(n).toMap());
        }

        myRef.updateChildren(values);
    }

    //Deletes RoomInformation on Database. Also deletes the RoomStatus value corresponding to it.
    public static void deleteRoomInformation(DatabaseReference myRef, RoomInformation room) {
        int roomNumber = room.getRoomNumber();
        DatabaseReference referencePoint;
        String childNode = "";

        //Delete RoomInformation value
        childNode = "/" + ROOMS_HEADER + "/" + roomNumber;
        referencePoint = myRef.child(childNode);
        referencePoint.removeValue();

        //Delete RoomStatus value
        childNode = "/" + STATUS_HEADER + "/" + roomNumber;
        referencePoint = myRef.child(childNode);
        referencePoint.removeValue();
    }

    //Reads reservation data found in Database. Returns ArrayList of Reservation values.
    public static ArrayList<Reservation> readReservationData(DataSnapshot snapshot) {

        DataSnapshot reservationNode = snapshot.child(RESERVATIONS_HEADER);
        ArrayList<Reservation> reservationList = new ArrayList<>();

        int inDay;
        int inMonth;
        int inYear;
        int outDay;
        int outMonth;
        int outYear;
        int assignedRoom;
        String name;
        double price;

        //Daily Reservations had two levels of children.
        //Level-1 header is the date. Level-2 header is the list of reservations for that date.
        for (DataSnapshot child : reservationNode.getChildren()) {

            for (DataSnapshot grandChild : child.getChildren()) {

                if (!child.getKey().equals(TABLE_HEADER)) {
                    try {
                        //manually parse data.
                        inDay = grandChild.child(IN_DAY).getValue(Integer.class);
                        inMonth = grandChild.child(IN_MONTH).getValue(Integer.class);
                        inYear = grandChild.child(IN_YEAR).getValue(Integer.class);
                        outDay = grandChild.child(OUT_DAY).getValue(Integer.class);
                        outMonth = grandChild.child(OUT_MONTH).getValue(Integer.class);
                        outYear = grandChild.child(OUT_YEAR).getValue(Integer.class);

                        assignedRoom = grandChild.child(ROOM_KEY).getValue(Integer.class);
                        price = grandChild.child(PRICE_KEY).getValue(Double.class);
                        name = grandChild.child(NAME_KEY).getValue(String.class);

                        reservationList.add(new Reservation(name, price,
                                DateUtilities.getDateObject(inYear, inMonth, inDay),
                                DateUtilities.getDateObject(outYear, outMonth, outDay), assignedRoom)
                        );
                    } catch (NumberFormatException e) {
                        //nothing, skip over element.
                        Log.e(TAG, e + " at " + grandChild.getKey());
                    } catch (NullPointerException e) {
                        Log.e(TAG, "null pointer exception at " + grandChild.getKey());
                    }
                }
            }

        }

        return reservationList;
    }

    public static ArrayList<Reservation> readReservationData(DataSnapshot snapshot, String dateStringFormat) {

        DataSnapshot reservationNode = snapshot.child(RESERVATIONS_HEADER);
        ArrayList<Reservation> reservationList = new ArrayList<>();

        int inDay = 0;
        int inMonth;
        int inYear;
        int outDay;
        int outMonth;
        int outYear;
        int assignedRoom;
        String name;
        double price;

        //Daily Reservations had two levels of children.
        //Level-1 header is the date. Level-2 header is the list of reservations for that date.
        DataSnapshot grandChild = reservationNode.child(dateStringFormat);

        for (DataSnapshot reservationChildren : grandChild.getChildren()) {
            if (!grandChild.getKey().equals(TABLE_HEADER)) {
                try {
                    //manually parse data.
                    inDay = reservationChildren.child(IN_DAY).getValue(Integer.class);
                    inMonth = reservationChildren.child(IN_MONTH).getValue(Integer.class);
                    inYear = reservationChildren.child(IN_YEAR).getValue(Integer.class);
                    outDay = reservationChildren.child(OUT_DAY).getValue(Integer.class);
                    outMonth = reservationChildren.child(OUT_MONTH).getValue(Integer.class);
                    outYear = reservationChildren.child(OUT_YEAR).getValue(Integer.class);

                    assignedRoom = reservationChildren.child(ROOM_KEY).getValue(Integer.class);
                    price = reservationChildren.child(PRICE_KEY).getValue(Double.class);
                    name = reservationChildren.child(NAME_KEY).getValue(String.class);

                    //Log.v(TAG, "adding ONE reservation");
                    reservationList.add(new Reservation(name, price,
                            DateUtilities.getDateObject(inYear, inMonth, inDay),
                            DateUtilities.getDateObject(outYear, outMonth, outDay), assignedRoom)
                    );
                } catch (NumberFormatException e) {
                    //nothing, skip over element.
                    Log.e(TAG, e + " at " + reservationChildren.getKey());
                } catch (NullPointerException e) {
                    Log.e(TAG, "null pointer exception." + reservationChildren.getKey());
                }
            }
        }

        return reservationList;
    }

    /* Returns the room numbers for all reservations that arrive on date specified under
    dateStringFormat and check-out the next day */
    private static ArrayList<Integer> readReservationDataConditional(DataSnapshot snapshot, Date tomorrow, String dateStringFormat) {
        ArrayList<Integer> myList = new ArrayList<>();
        DataSnapshot reservationNode = snapshot.child(RESERVATIONS_HEADER);
        DataSnapshot grandChild = reservationNode.child(dateStringFormat);

        int nextDay = DateUtilities.getDay(tomorrow);
        int nextMonth = DateUtilities.getMonth(tomorrow);
        int nextYear = DateUtilities.getYear(tomorrow);

        int outDay;
        int outMonth;
        int outYear;
        int assignedRoom;

        for (DataSnapshot reservationChildren : grandChild.getChildren()) {
            if (!grandChild.getKey().equals(TABLE_HEADER)) {
                try {
                    //manually parse data.
                    outDay = reservationChildren.child(OUT_DAY).getValue(Integer.class);
                    outMonth = reservationChildren.child(OUT_MONTH).getValue(Integer.class);
                    outYear = reservationChildren.child(OUT_YEAR).getValue(Integer.class);

                    assignedRoom = reservationChildren.child(ROOM_KEY).getValue(Integer.class);

                    if (outYear == nextYear && outMonth == nextMonth && outDay == nextDay) {
                        myList.add(assignedRoom);
                    }

                } catch (NumberFormatException e) {
                    //nothing, skip over element.
                    Log.e(TAG, e + " at " + reservationChildren.getKey());
                } catch (NullPointerException e) {
                    Log.e(TAG, "null pointer exception." + reservationChildren.getKey());
                }
            }
        }

        return myList;
    }

    public static int getTotalReservations(DataSnapshot snapshot, String dateStringFormat) {

        DataSnapshot reservationNode = snapshot.child(RESERVATIONS_HEADER);

        //Daily Reservations had two levels of children.
        //Level-1 header is the date. Level-2 header is the list of reservations for that date.
        DataSnapshot grandChild = reservationNode.child(dateStringFormat);
        //One of these children is the 'tableHeader' placeholder. subtract one to get correct value.
        long childCount = reservationNode.getChildrenCount() - 1;

        return (int) childCount;
    }

    //Adds information of a single reservation to database.
    public static void updateReservation(DatabaseReference myRef, Reservation reservation) {
        //Formats index keys.
        String childNode = "/" + RESERVATIONS_HEADER + "/" +
                DateUtilities.generateTableHeader(reservation.getCheckInDate())
                + "/" + reservation.getAssignedRoom();

        Map<String, Object> values = new HashMap<>();
        values.put(childNode, reservation.toMap());
        myRef.updateChildren(values);

        //Calculate number of nights in reservation.
        int nights = DateUtilities.calculateDifferenceInDays(reservation.getCheckInDate(), reservation.getCheckOutDate());

        //Adds values of unavailability to the days between check-in and check-out.
        //Prevents creating of overlapping reservations.
        String blockNode;
        Date tempDate = reservation.getCheckInDate();
        int d = DateUtilities.getDay(tempDate);
        int m = DateUtilities.getMonth(tempDate);
        int y = DateUtilities.getYear(tempDate);

        Map<String, Object> blockValues = new HashMap<>();
        for (int k = 0; k < nights; k++) {
            blockNode = "/" + UNAVAILABLE_HEADER + "/" +
                    DateUtilities.generateTableHeader(DateUtilities.getDateObject(y, m, d + k))
                    + "/" + reservation.getAssignedRoom();

            blockValues.put(blockNode, reservation.getAssignedRoom());
        }

        myRef.updateChildren(blockValues);

    }

    //Adds Reservation information from a list and adds to database.
    public static void updateReservations(DatabaseReference
                                                  myRef, ArrayList<Reservation> reservations) {
        String childNode;
        int totalReservation = reservations.size();
        Map<String, Object> values = new HashMap<>();


        for (int r = 0; r < totalReservation; r++) {
            //Format child node properly for reservation indexed at r.
            childNode = "/dailyReservations/" +
                    DateUtilities.generateTableHeader(reservations.get(r).getCheckInDate())
                    + "/" + reservations.get(r).getAssignedRoom();
            values.put(childNode, reservations.get(r).toMap());
        }

        myRef.updateChildren(values);
    }

    public static void deleteReservation(DatabaseReference myRef, Reservation reservation) {

        Log.v("DBSU", "DELETING!!!!");

        /*         int roomNumber = room.getRoomNumber();
        DatabaseReference referencePoint;
        String childNode = "";

        //Delete RoomInformation value
        childNode = "/" + ROOMS_HEADER + "/" + roomNumber;
        referencePoint = myRef.child(childNode);
        referencePoint.removeValue();
        */

        DatabaseReference referencePoint;

        //Locates reservation and deletes it.
        String childNode = "/" + RESERVATIONS_HEADER + "/" +
                DateUtilities.generateTableHeader(reservation.getCheckInDate())
                + "/" + reservation.getAssignedRoom();

        referencePoint = myRef.child(childNode);
        referencePoint.removeValue();


        //Calculate number of nights in reservation.
        int nights = DateUtilities.calculateDifferenceInDays(reservation.getCheckInDate(), reservation.getCheckOutDate());

        //Adds values of unavailability to the days between check-in and check-out.
        //Prevents creating of overlapping reservations.
        String blockNode;
        Date tempDate = reservation.getCheckInDate();
        int d = DateUtilities.getDay(tempDate);
        int m = DateUtilities.getMonth(tempDate);
        int y = DateUtilities.getYear(tempDate);

        int roomNumber = reservation.getAssignedRoom();
        //Deletes each instance needed for that room.
        for (int k = 0; k < nights; k++) {
            blockNode = "/" + UNAVAILABLE_HEADER + "/" +
                    DateUtilities.generateTableHeader(DateUtilities.getDateObject(y, m, d + k))
                    + "/" + roomNumber;
            myRef.child(blockNode).removeValue();
        }
    }

    //Removes rooms from 'unavailable' list with start day for
    //n number of days.
    public static void deleteUnavailable(DatabaseReference myRef, Date start, int n,
                                         int roomNumber) {
        DatabaseReference referencePoint;
        //Adds values of unavailability to the days between check-in and check-out.
        //Prevents creating of overlapping reservations.
        String blockNode;
        int d = DateUtilities.getDay(start);
        int m = DateUtilities.getMonth(start);
        int y = DateUtilities.getYear(start);

        Map<String, Object> blockValues = new HashMap<>();
        for (int k = 0; k < n; k++) {
            blockNode = "/" + UNAVAILABLE_HEADER + "/" +
                    DateUtilities.generateTableHeader(DateUtilities.getDateObject(y, m, d + k))
                    + "/" + roomNumber;

            referencePoint = myRef.child(blockNode);
            referencePoint.removeValue();
        }

    }

    private static ArrayList<Integer> readUnavailableForDate(DataSnapshot snapshot, Date date) {
        ArrayList<Integer> myList = new ArrayList<>();
        String formattedHeader = DateUtilities.generateTableHeader(date);
        DataSnapshot unavailableNode = snapshot.child(UNAVAILABLE_HEADER);
        DataSnapshot dateNode = unavailableNode.child(formattedHeader);

        //go through all rooms.
        for (DataSnapshot child : dateNode.getChildren()) {


            //Read keys. In this node values are the same and are integers.
            try {
                myList.add(child.getValue(Integer.class));
            } catch (Exception e) {
                Log.e("DBSU", e.toString());
            }
        }

        return myList;
    }

    public static ArrayList<RoomStatus> readStatusData(DataSnapshot snapshot) {
        DataSnapshot statusNode = snapshot.child(STATUS_HEADER);
        ArrayList<RoomStatus> statusList = new ArrayList<>();
        for (DataSnapshot child : statusNode.getChildren()) {

            if (!child.getKey().equals(TABLE_HEADER))
                statusList.add(child.getValue(RoomStatus.class));
        }
        return statusList;
    }

    public static String[] readSummaryData(DataSnapshot snapshot, int request_code) {

        String[] returnValues = null;

        DataSnapshot summmaryNode = snapshot.child(SUMMARY_HEADER);
        switch (request_code) {
            case COMPLETE_SUMMARY:
                returnValues = new String[]{"0", "0", "0", "0", "0", "0", "0", "0"};
                break;
            case SIMPLE_SUMMARY:
                returnValues = new String[]{"0", "0"};
        }

        for (DataSnapshot child : summmaryNode.getChildren()) {


            if (child.getKey().equals(NAME_HEADER)) {
                returnValues[0] = child.getValue(String.class);
            } else if (child.getKey().equals(TOTAL_KEY)) {
                int k = child.getValue(Integer.class);
                returnValues[1] = "" + k;
            }

            //Only handle extra values if the function calls for a complete summary.
            else if (request_code == COMPLETE_SUMMARY) {
                switch (child.getKey()) {
                    case SINGLE_BED_KEY:
                        returnValues[2] = "" + child.getValue(Integer.class);
                        break;
                    case DOUBLE_BED_KEY:
                        returnValues[3] = "" + child.getValue(Integer.class);
                        break;
                    case SINGLE_SMOKING_KEY:
                        returnValues[4] = "" + child.getValue(Integer.class);
                        break;
                    case DOUBLE_SMOKING_KEY:
                        returnValues[5] = "" + child.getValue(Integer.class);
                        break;
                    case SUITE_KEY:
                        returnValues[6] = "" + child.getValue(Integer.class);
                        break;
                    case SUITE_SMOKING_KEY:
                        returnValues[7] = "" + child.getValue(Integer.class);
                }
            }
        }

        return returnValues;
    }

    public static void updateSummaryData(DatabaseReference reference, String hotelName, int[] roomTotals) {
        String databaseChild = "/summary/";

        int totalRooms = 0;
        for (int n : roomTotals)
            totalRooms += n;

        Map<String, Object> values = new HashMap<>();
        values.put(databaseChild + NAME_HEADER, hotelName);
        values.put(databaseChild + SINGLE_BED_KEY, roomTotals[0]);
        values.put(databaseChild + DOUBLE_BED_KEY, roomTotals[1]);
        values.put(databaseChild + SINGLE_SMOKING_KEY, roomTotals[2]);
        values.put(databaseChild + DOUBLE_SMOKING_KEY, roomTotals[3]);
        values.put(databaseChild + SUITE_KEY, roomTotals[4]);
        values.put(databaseChild + SUITE_SMOKING_KEY, roomTotals[5]);
        values.put(databaseChild + TOTAL_KEY, totalRooms);

        reference.updateChildren(values);
    }

    //Updates information of all unoccupied rooms and sets to clean.
    public static void cleanRooms(DatabaseReference myRef, ArrayList<RoomStatus> rooms) {
        String databaseChild = "/dailyStatus/";
        String childNode = "";
        int tempRoom = 0;

        Map<String, Object> values = new HashMap<>();

        for (int i = 0; i < rooms.size(); i++) {
            tempRoom = rooms.get(i).getRoomNumber();
            childNode = databaseChild + tempRoom;

            //Only updates if the room shows not OCCUPIED.
            if (rooms.get(i).getRoomStatus() != RoomStatus.OCCUPIED)
                values.put(childNode, new RoomStatus(tempRoom, RoomStatus.CLEAN).toMap());
        }

        myRef.updateChildren(values);
    }

    public static void checkOutAllGuests(DataSnapshot snapshot, DatabaseReference myRef, Date today) {

        int year = DateUtilities.getYear(today);
        int month = DateUtilities.getMonth(today);
        int day = DateUtilities.getDay(today);

        //Adjust one day prior.
        Date yesterday = DateUtilities.getDateObject(year, month, day - 1);

        ArrayList<Integer> roomIndexes;
        ArrayList<Integer> unavailableIndexes;
        ArrayList<RoomStatus> listToUpdate;

        /* The full list to update is composed of two conditions
        1 all rooms which were unavailable yesterday (meaning a reservation checks out today)
        2 all rooms checking in yesterday with checkout today.
         */

        //Step 1: read all unavailable rooms for yesterday
        unavailableIndexes = readUnavailableForDate(snapshot, yesterday);

        //Step 2 read reservations arriving today. get numbers for those which leave today.
        roomIndexes = readReservationDataConditional(snapshot, today, DateUtilities.generateTableHeader(today));

        //Step 3 combine list as status list.
        roomIndexes.addAll(unavailableIndexes);

        //Generate RoomStatus array list from indexes.
        listToUpdate = RoomStatus.generateListFromIndexes(roomIndexes, RoomStatus.DIRTY);

        //Push updates to database.
        updateStatus(myRef, listToUpdate);
    }

    //Updates RoomStatus information saved to database for a singe entry
    public static void updateStatus(DatabaseReference myRef, RoomStatus room) {

        String databaseChild = "/dailyStatus/";
        databaseChild = databaseChild + room.getRoomNumber();

        Map<String, Object> values = new HashMap<>();
        values.put(databaseChild, room.toMap());

        myRef.updateChildren(values);
    }

    //Updates RoomStatus information saved to database for a list.
    public static void updateStatus(DatabaseReference myRef, ArrayList<RoomStatus> rooms) {
        String databaseChild = "/dailyStatus/";
        String childNode = "";
        int tempRoom = 0;

        Map<String, Object> values = new HashMap<>();

        for (int i = 0; i < rooms.size(); i++) {
            tempRoom = rooms.get(i).getRoomNumber();
            childNode = databaseChild + tempRoom;

            values.put(childNode, rooms.get(i).toMap());
        }

        myRef.updateChildren(values);
    }
}
