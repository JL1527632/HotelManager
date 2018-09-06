package com.leon.example.orangehotel.SupportUtilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.firebase.database.DataSnapshot;

public class SummaryLoader extends AsyncTaskLoader<String[]> {

    private DataSnapshot mDataSnapshot;
    String[] myReturnValues;

    public SummaryLoader(Context context, DataSnapshot snapshot) {
        super(context);
        mDataSnapshot = snapshot;

    }

    @Override
    public String[] loadInBackground() {

        myReturnValues = DatabaseSupportUtilities.readSummaryData(mDataSnapshot, DatabaseSupportUtilities.COMPLETE_SUMMARY);
        return myReturnValues;
    }
}
