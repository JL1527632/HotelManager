package com.leon.example.orangehotel.DataStructures;

import android.content.Context;
import android.util.Log;

import com.leon.example.orangehotel.R;

public class RoomTypes {
    public static final int SINGLE_BED = 100;
    public static final int DOUBLE_BED = 200;
    public static final int SINGLE_SMOKING = 300;
    public static final int DOUBLE_SMOKING = 400;
    public static final int SUITE = 500;
    public static final int SUITE_SMOKING = 600;

    public static final int SINGLE_INDEX = 0;
    public static final int DOUBLE_INDEX = 1;
    public static final int SINGLE_SMOKING_INDEX = 2;
    public static final int DOUBLE_SMOKING_INDEX = 3;
    public static int SUITE_INDEX = 4;
    public static int SUITE_SMOKING_INDEX = 5;

    public static final int[] TYPE_ARRAY = new int[]{SINGLE_BED, DOUBLE_BED, SINGLE_SMOKING, DOUBLE_SMOKING, SUITE, SUITE_SMOKING};

    public static String typeAsString(Context context, int type) {
        String value = "";

        int adjusted = type % 100;
        int calculated = type - adjusted;

        if (context == null)
            Log.v("RType", "context is null");

        switch (calculated) {
            case SINGLE_BED:
                value = context.getString(R.string.room_type_sb);
                break;
            case DOUBLE_BED:
                value = context.getString(R.string.room_type_db);
                break;
            case SINGLE_SMOKING:
                value = context.getString(R.string.room_type_ss);
                break;
            case DOUBLE_SMOKING:
                value = context.getString(R.string.room_type_ds);
                break;
            case SUITE:
                value = context.getString(R.string.room_type_ste);
                break;
            case SUITE_SMOKING:
                value = context.getString(R.string.room_type_sts);
        }
        return value;
    }

}