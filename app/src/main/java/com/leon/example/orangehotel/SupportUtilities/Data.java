package com.leon.example.orangehotel.SupportUtilities;

public class Data {

    public static final int UPDATE_EDIT_RESERVATION_REQUEST = 800;

    public static HeaderClass getTableHeader() {
        return new HeaderClass();
    }

    private static class HeaderClass {

        int tableHeader;

        HeaderClass() {
            tableHeader = 0;
        }
    }

    public static SummaryHeader getSummaryTableHeader() {
        return new SummaryHeader();
    }

    private static class SummaryHeader {

        String HOTEL_NAME;
        String TOTAL_ROOMS;
        String SINGLE_BED;
        String DOUBLE_BED;
        String SINGLE_SMOKING;
        String DOUBLE_SMOKING;
        String SUITE;
        String SUITE_SMOKING;

        SummaryHeader() {
            HOTEL_NAME = "HOTEL NAME";
            TOTAL_ROOMS = "0";
            SINGLE_BED = "0";
            DOUBLE_BED = "0";
            SINGLE_SMOKING = "0";
            DOUBLE_SMOKING = "0";
            SUITE = "0";
            SUITE_SMOKING = "0";
        }
    }

}
