package com.leon.example.orangehotel.SupportUtilities;

import com.leon.example.orangehotel.DataStructures.DailyDetails;
import com.leon.example.orangehotel.DataStructures.Reservation;
import com.leon.example.orangehotel.DataStructures.RoomInformation;
import com.leon.example.orangehotel.DataStructures.RoomStatus;
import com.leon.example.orangehotel.DataStructures.RoomTypes;

import java.util.ArrayList;

public class SampleData {

    public static void logTestDailyDetailsReservations() {

        //DailyDetails(RoomInformation roomInformation, Reservation reservation, RoomStatus status
        DailyDetails detailsOne = new DailyDetails(new RoomInformation(463, RoomTypes.DOUBLE_BED),
                new Reservation("Golf Gerryville", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10)), null) ;
        System.out.println("No reservations present in reservation one ");
        //Log.v("ONE", "no reservation present");

        if (detailsOne.hasReservation())
            System.out.println("TEST: Reservation Present");
            //Log.v("ONE", "reservation present");
        else
            System.out.println("TEST: Reservation not present");
        //Log.v("ONE", "no reservation present");

        DailyDetails detailsTwo = new DailyDetails(new RoomInformation(463, RoomTypes.DOUBLE_BED),
                new Reservation("Golf Gerryville", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10)), null) ;
        System.out.println("No reservations present in reservation one ");

        if (detailsOne.hasReservation())
            System.out.println("TEST: Reservation Present");
        else
            System.out.println("TEST: Reservation not present");

        DailyDetails detailsThree = new DailyDetails(new RoomInformation(463, RoomTypes.DOUBLE_BED),
                null, null) ;
        System.out.println("No reservations present in reservation one ");
        //Log.v("ONE", "no reservation present");

        if (detailsOne.hasReservation())
            System.out.println("TEST: Reservation Present");
        else
            System.out.println("TEST: Reservation not present");

        DailyDetails detailsFour = new DailyDetails(new RoomInformation(463, RoomTypes.DOUBLE_BED),
                new Reservation("Delta Danields", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10)), null) ;
        System.out.println("Reservations present in reservation four ");
        //Log.v("ONE", "no reservation present");

        if (detailsOne.hasReservation())
            System.out.println("TEST: Reservation Present");
        else
            System.out.println("TEST: Reservation not present");

        DailyDetails detailsFive = new DailyDetails(new RoomInformation(463, RoomTypes.DOUBLE_BED),
                new Reservation("Echo Edwards", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10)), null) ;
        System.out.println("Rservations present in reservation five ");
        //Log.v("ONE", "no reservation present");

        if (detailsOne.hasReservation())
            System.out.println("TEST: Reservation Present");
        else
            System.out.println("TEST: Reservation not present");
    }

    public static Reservation getTestReservationNoNumber() {
        return new Reservation("James Wilson", 24.00, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10));
    }

    public static Reservation getTestReservationWithNumber(int k)
    {
        return new Reservation("Rhonda Rhimes", 55.00, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10), k );
    }

    public static ArrayList<Reservation> getTestReservationList() {
        ArrayList<Reservation> myList = new ArrayList<>();
        myList.add(new Reservation("Alpha Almond", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10), 734));
        myList.add(new Reservation("Bravo Baltimore", 22.00, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10), 735));
        myList.add(new Reservation("Charlie Cleveland", 13.50, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 11)));
        myList.add(new Reservation("Delta Dallas", 18.35, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 9), 737));
        myList.add(new Reservation("Echo El Paso", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10)));
        //myList.add(new Reservation("Frank Philadelphia", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10),739));
        //myList.add(new Reservation("Golf Gerryville", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10),740));
        return myList;
    }

    public static ArrayList<Reservation> getControlledTestReservationList() {
        ArrayList<Reservation> myList = new ArrayList<>();
        myList.add(new Reservation("Alpha Almond", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10), 734));
        myList.add(new Reservation("Bravo Baltimore", 22.00, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10), 735));
        myList.add(new Reservation("Charlie Cleveland", 13.50, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 11), 736));
        myList.add(new Reservation("Delta Dallas", 18.35, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 9), 737));
        myList.add(new Reservation("Echo El Paso", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10), 738));
        return myList;
    }

    public static ArrayList<Reservation> getTestReservationListLong()
    {
        ArrayList<Reservation> myList = new ArrayList<>();
        myList.add(new Reservation("Alpha Almond", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10), 734));
        myList.add(new Reservation("Bravo Baltimore", 22.00, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10), 735));
        myList.add(new Reservation("Charlie Cleveland", 13.50, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 11), 736));
        myList.add(new Reservation("Delta Dallas", 18.35, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 9), 737));
        myList.add(new Reservation("Echo El Paso", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10), 738));
        myList.add(new Reservation("Frank Philadelphia", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10),739));
        myList.add(new Reservation("Golf Gerryville", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10),740));
        myList.add(new Reservation("Henry Hotel", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10),741));
        myList.add(new Reservation("Indiana Illinois", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10),742));
        myList.add(new Reservation("Jeffrey Jacksonville", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10),743));
        myList.add(new Reservation("Kimberly Kilogram", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10),745));
        return myList;
    }

    public static RoomInformation getRoomInformationWithNumber( int r)
    {
        return new RoomInformation( r, RoomTypes.DOUBLE_BED );
    }

    public static ArrayList<RoomInformation> getTestRoomDetailsList() {
        //733 - 740.

        ArrayList<RoomInformation> myRoomList = new ArrayList<>();

        myRoomList.add(new RoomInformation(733, RoomTypes.DOUBLE_BED));
        myRoomList.add(new RoomInformation(734, RoomTypes.SINGLE_BED));
        myRoomList.add(new RoomInformation(735, RoomTypes.SINGLE_SMOKING));
        myRoomList.add(new RoomInformation(736, RoomTypes.SINGLE_BED));
        myRoomList.add(new RoomInformation(737, RoomTypes.DOUBLE_BED));
        myRoomList.add(new RoomInformation(738, RoomTypes.SUITE));
        myRoomList.add(new RoomInformation(739, RoomTypes.DOUBLE_BED));
        myRoomList.add(new RoomInformation(740, RoomTypes.SINGLE_SMOKING));
        return myRoomList;
    }

    public static ArrayList<RoomInformation> getTestRoomDetailsListLong() {
        //730 - 750.
        ArrayList<RoomInformation> myRoomList = new ArrayList<>();

        myRoomList.add(new RoomInformation(730, RoomTypes.DOUBLE_BED));
        myRoomList.add(new RoomInformation(731, RoomTypes.SINGLE_BED));
        myRoomList.add(new RoomInformation(732, RoomTypes.SINGLE_SMOKING));
        myRoomList.add(new RoomInformation(733, RoomTypes.DOUBLE_BED));
        myRoomList.add(new RoomInformation(734, RoomTypes.SINGLE_BED));
        myRoomList.add(new RoomInformation(735, RoomTypes.SINGLE_SMOKING));
        myRoomList.add(new RoomInformation(736, RoomTypes.SINGLE_BED));
        myRoomList.add(new RoomInformation(737, RoomTypes.DOUBLE_BED));
        myRoomList.add(new RoomInformation(738, RoomTypes.SINGLE_SMOKING));
        myRoomList.add(new RoomInformation(739, RoomTypes.DOUBLE_BED));
        myRoomList.add(new RoomInformation(740, RoomTypes.SINGLE_SMOKING));
        myRoomList.add(new RoomInformation(741, RoomTypes.SINGLE_BED));
        myRoomList.add(new RoomInformation(742, RoomTypes.DOUBLE_BED));
        myRoomList.add(new RoomInformation(743, RoomTypes.DOUBLE_BED));
        myRoomList.add(new RoomInformation(743, RoomTypes.SINGLE_SMOKING));
        myRoomList.add(new RoomInformation(744, RoomTypes.DOUBLE_SMOKING));
        myRoomList.add(new RoomInformation(745, RoomTypes.DOUBLE_SMOKING));
        myRoomList.add(new RoomInformation(746, RoomTypes.SUITE));
        myRoomList.add(new RoomInformation(747, RoomTypes.SUITE));
        myRoomList.add(new RoomInformation(748, RoomTypes.SUITE));
        myRoomList.add(new RoomInformation(749, RoomTypes.SUITE_SMOKING));
        myRoomList.add(new RoomInformation(750, RoomTypes.SUITE_SMOKING));
        return myRoomList;
    }

    /** WHAT GETS RETURNED **/
    public static ArrayList<DailyDetails> getTestDailyDetailsList() {
        ArrayList<DailyDetails> myList = new ArrayList<>();

        myList.add(new DailyDetails(new RoomInformation(225, RoomTypes.DOUBLE_SMOKING),
                new Reservation("Alpha Alvaro", 50.00, DateUtilities.getDateObject(2015, 5, 5), DateUtilities.getDateObject(2016, 8, 1)), new RoomStatus(225, RoomStatus.DIRTY)) );

        myList.add(new DailyDetails(new RoomInformation(226, RoomTypes.DOUBLE_SMOKING), null, new RoomStatus(226, RoomStatus.CLEAN)));

        myList.add(new DailyDetails(new RoomInformation(227, RoomTypes.DOUBLE_SMOKING),
                new Reservation("Charlie Chevrolet", 50.00, DateUtilities.getDateObject(2013, 8, 3), DateUtilities.getDateObject(2016, 6, 10)), new RoomStatus(227, RoomStatus.DIRTY)) );

        myList.add(new DailyDetails(new RoomInformation(228, RoomTypes.DOUBLE_SMOKING),
                new Reservation("Delta December", 50.00, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 10, 10)), new RoomStatus(228, RoomStatus.CLEAN)) );

        myList.add(new DailyDetails(new RoomInformation(229, RoomTypes.DOUBLE_SMOKING), null, new RoomStatus(229, RoomStatus.DIRTY)));

        return myList;
    }

    /*
    public static Map<String, Object> getTestMapData() {
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("900", new RoomInformation(900, RoomTypes.DOUBLE_SMOKING, RoomInformation.CLEAN));
        mapData.put("800", new RoomInformation(800, RoomTypes.DOUBLE_SMOKING, RoomInformation.DIRTY));
        return mapData;
    } */

    public static void logReservations() {
        System.out.println("Checking data on reservations. Running verifications.");
        //Log.v("RLA", "checking data on all reservations");

        System.out.println("Expecting valid data");
        Reservation ReservationOne = new Reservation("Alpha Gerryville", 11.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 9));
        if (Reservation.verifyData(ReservationOne) )
            System.out.println("Found as valid");
        else
            System.out.println("Found as invalid.");

        System.out.println("Expecting invalid data");
        Reservation ReservationTwo = new Reservation("Bravo Gerryville", -5.65, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10));
        if (Reservation.verifyData(ReservationTwo) )
            System.out.println("Found as valid");
        else
            System.out.println("Found as invalid.");

        System.out.println("Expecting valid data");
        Reservation ReservationThree = new Reservation("Charlie Gerryville", 31.11, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 11));
        if (Reservation.verifyData(ReservationThree) )
            System.out.println("Found as valid");
        else
            System.out.println("Found as invalid.");

        System.out.println("Expecting invalid data");
        Reservation ReservationFour = new Reservation("Delta Gerryville", 44.60, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 8));
        if (Reservation.verifyData(ReservationFour) )
            System.out.println("Found as valid");
        else
            System.out.println("Found as invalid.");

        System.out.println("Expecting invalid data");
        Reservation ReservationFive = new Reservation("Echo Gerryville", 55.90, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2016, 8, 13));
        if (Reservation.verifyData(ReservationFive) )
            System.out.println("Found as valid");
        else
            System.out.println("Found as invalid.");
    }

    public static DailyDetails getTestDailyDetails() {
        DailyDetails detailsOne = new DailyDetails(new RoomInformation(901, RoomTypes.DOUBLE_SMOKING),
                new Reservation("Single Daily", 50.00, DateUtilities.getDateObject(2018, 8, 8), DateUtilities.getDateObject(2018, 8, 10), 901), new RoomStatus(901, RoomStatus.DIRTY));

        return detailsOne;
    }

    public static ArrayList<RoomStatus> getRoomStatusList()
    {
        ArrayList<RoomStatus> list = new ArrayList<>();
        //(int roomNumber, int status)
        list.add ( new RoomStatus (467, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (501, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (32, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (152, RoomStatus.OCCUPIED) );
        list.add ( new RoomStatus (243, RoomStatus.OCCUPIED) );
        list.add ( new RoomStatus (912, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (73, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (31, RoomStatus.OCCUPIED) );

        return list;
    }

    public static ArrayList<RoomStatus> getControlledRoomStatusList()
    {
        ArrayList<RoomStatus> list = new ArrayList<>();
        //(int roomNumber, int status)
        list.add ( new RoomStatus (730, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (731, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (732, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (733, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (734, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (735, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (736, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (737, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (738, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (739, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (740, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (741, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (742, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (743, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (744, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (745, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (746, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (747, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (748, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (749, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (750, RoomStatus.CLEAN) );
        return list;
    }

    public static ArrayList<RoomStatus> getDuplicateRoomStatusList()
    {
        ArrayList<RoomStatus> list = new ArrayList<>();
        //(int roomNumber, int status)
        list.add ( new RoomStatus (733, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (734, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (735, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (735, RoomStatus.CLEAN) );
        list.add ( new RoomStatus (737, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (738, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (740, RoomStatus.DIRTY) );
        list.add ( new RoomStatus (740, RoomStatus.DIRTY) );
        return list;
    }

    public static RoomStatus getRoomStatus(int r)
    {
        return new RoomStatus( r, RoomStatus.CLEAN);
    }
}
