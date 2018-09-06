package com.leon.example.orangehotel.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leon.example.orangehotel.DataStructures.Reservation;
import com.leon.example.orangehotel.DataStructures.RoomInformation;
import com.leon.example.orangehotel.DataStructures.RoomStatus;
import com.leon.example.orangehotel.R;
import com.leon.example.orangehotel.SupportUtilities.DatabaseSupportUtilities;
import com.leon.example.orangehotel.SupportUtilities.DateUtilities;

import java.util.ArrayList;

public class DetailsEditorActivity extends AppCompatActivity implements DetailsEditorFragment.OnSavedConfirmed, DatePickerFragment.OnDateSelectionListener {

    public final int SAVE_SELECTED = 702;

    RoomInformation mInformation;
    Reservation mReservation;
    RoomStatus mRoomStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DetailsEditorFragment fragment = new DetailsEditorFragment();
        Intent callingIntent = getIntent();

        if (callingIntent != null) {

            if (callingIntent.hasExtra(getString(R.string.extra_information))) {
                mInformation = callingIntent.getParcelableExtra(getString(R.string.extra_information));
                mReservation = callingIntent.getParcelableExtra(getString(R.string.extra_reservation));
                mRoomStatus = callingIntent.getParcelableExtra(getString(R.string.extra_status));

            } else {
                mInformation = null;
                mReservation = null;
                mRoomStatus = null;
            }
        }

        //If there is no existing data, assign information to fragment.
        if (savedInstanceState == null) {
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            fragment.addData(mInformation, mReservation);
            manager.beginTransaction()
                    .add(R.id.room_list_fragment, fragment)
                    .commit();
        }
    }

    //Updates reservation information. Pushes updates to database.
    //Allows for the user to exit in this screen without any changes being lost.
    @Override
    public void updateData(Reservation updatedReservationDetails, Boolean dateChanged, int selection) {

        if (selection == SAVE_SELECTED) {
            mReservation = updatedReservationDetails;

            Intent resultIntent = new Intent();
            ArrayList<Reservation> myData = new ArrayList<>();
            myData.add(mReservation);
            resultIntent.putParcelableArrayListExtra(getString(R.string.extra_reservation), myData);
            setResult(RESULT_OK, resultIntent);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            //First see if we have to edit the blocked rooms.
            if (dateChanged) {
                DatabaseSupportUtilities.deleteUnavailable(reference, updatedReservationDetails.getCheckInDate(),
                        DateUtilities.calculateDifferenceInDays(updatedReservationDetails.getCheckInDate(),
                                updatedReservationDetails.getCheckOutDate()),
                        updatedReservationDetails.getAssignedRoom()
                );
            }

            //Push updates to database
            DatabaseSupportUtilities.updateReservation(reference, mReservation);

            //Close activity.
            finish();
        }
        else
        {
            //This selection should not run on small layouts, only large.
            Log.e("DEA", "Fragment called a cancel operation on small layout");
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        DialogInterface.OnClickListener discardChangesListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardChangesListener);

    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_information);
        builder.setPositiveButton(R.string.unsaved_confirm, listener);
        builder.setNegativeButton(R.string.unsaved_stay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Used inside
    @Override
    public void onDateSelected(int selection, int year, int month, int day) {

        //Read fragment
        FragmentManager manager = getSupportFragmentManager();
        DetailsEditorFragment mFragment = (DetailsEditorFragment) manager.findFragmentById(R.id.room_list_fragment);

        mFragment.addNewDate(selection, year, month, day);

    }
}
