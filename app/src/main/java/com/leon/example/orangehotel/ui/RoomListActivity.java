package com.leon.example.orangehotel.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leon.example.orangehotel.DataStructures.DailyDetails;
import com.leon.example.orangehotel.DataStructures.Reservation;
import com.leon.example.orangehotel.R;
import com.leon.example.orangehotel.SupportUtilities.DatabaseSupportUtilities;
import com.leon.example.orangehotel.SupportUtilities.DateUtilities;

public class RoomListActivity extends AppCompatActivity implements RoomListFragment.OnDetailSelectedListener,
        RoomListFragment.OnDateChanged, RoomSummaryFragment.OnChangeRequestedListener, RoomSummaryFragment.OnDeleteRequestedListener,
        DetailsEditorFragment.OnSavedConfirmed,
        DatePickerFragment.OnDateSelectionListener {

    final static int DD_LOADER = 2200;
    private final int CLEAN_ROOMS_ACTION = 14;
    private final int CHECK_OUT_ACTION = 13;
    public final int CANCEL_SELECTED = 701;
    public final int SAVE_SELECTED = 702;

    String dailyString;
    final String TAG = "RLActvity";
    boolean tabletLayout;
    DailyDetails myDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks to see if higher resolution layout is enabled.
        tabletLayout = (findViewById(R.id.summary_editor_container) != null);

        if (savedInstanceState == null) {
            Log.v("RLA", "fragment creation called");
            RoomListFragment fragment = new RoomListFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

            manager.beginTransaction()
                    .add(R.id.room_list_fragment, fragment)
                    .commit();

            if (tabletLayout) {
                RoomSummaryFragment summaryFragment = new RoomSummaryFragment();
                manager.beginTransaction()
                        .add(R.id.summary_editor_container, summaryFragment)
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_room_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettingsActivity();
                return true;
            case R.id.action_clean:
                confirmAction(CLEAN_ROOMS_ACTION);
                return true;
            case R.id.action_checkout:
                confirmAction(CHECK_OUT_ACTION);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openRoomDetailsActivity() {
        Context context = this;

        Class destinationClass = DetailsEditorActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        startActivity(intentToStartDetailActivity);
    }

    private void openDataBaseActivity() {
        Context context = this;

        Class destinationClass = DatabaseInteractionActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        startActivity(intentToStartDetailActivity);
    }

    private void openSettingsActivity() {
        Context context = this;
        Class destinationClass = AboutHotelActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        startActivity(intentToStartDetailActivity);
    }

    //Interface called from Fragment to trigger launch of details.
    @Override
    public void onDetailSelected(DailyDetails myDetails) {

        this.myDetails = myDetails;
        Context context = this;
        Class destinationClass = RoomSummaryActivity.class;

        if (!tabletLayout) {
            Intent intentToStartDetailActivity = new Intent(context, destinationClass);

            intentToStartDetailActivity.putExtra(getString(R.string.extra_information), myDetails.getRoomInformation());
            intentToStartDetailActivity.putExtra(getString(R.string.extra_reservation), myDetails.getReservationDetails());
            intentToStartDetailActivity.putExtra(getString(R.string.extra_status), myDetails.getRoomStatusObject());
            startActivity(intentToStartDetailActivity);
        }
        //In tablet layout, do not open new activity. Update fragments
        else {
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            RoomSummaryFragment fragment = new RoomSummaryFragment();
            fragment.assignData(myDetails.getRoomInformation(), myDetails.getReservationDetails(), myDetails.getRoomStatusObject());
            manager.beginTransaction()
                    .replace(R.id.summary_editor_container, fragment)
                    .commit();
        }

    }

    //Replaces summary fragment with Editor Fragment.
    @Override
    public void launchEditActivity() {

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        DetailsEditorFragment fragment = new DetailsEditorFragment();
        fragment.addData(myDetails.getRoomInformation(), myDetails.getReservationDetails());
        manager.beginTransaction()
                .replace(R.id.summary_editor_container, fragment)
                .commit();
    }


    //Called by DetailsEditor Fragment.
    @Override
    public void updateData(Reservation updatedReservationDetails, Boolean dateChanged, int option) {

        if (option == SAVE_SELECTED) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            if (dateChanged) {
                //First see if we have to edit the blocked rooms.
                DatabaseSupportUtilities.deleteUnavailable(reference, updatedReservationDetails.getCheckInDate(),
                        DateUtilities.calculateDifferenceInDays(updatedReservationDetails.getCheckInDate(),
                                updatedReservationDetails.getCheckOutDate()),
                        updatedReservationDetails.getAssignedRoom()
                );
            }
            //Push updates to database
            DatabaseSupportUtilities.updateReservation(reference, updatedReservationDetails);

            myDetails.setReservationData(updatedReservationDetails);
            //Close fragment_editor fragment and replace with summary fragment.
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            RoomSummaryFragment fragment = new RoomSummaryFragment();
            fragment.assignData(myDetails.getRoomInformation(), myDetails.getReservationDetails(), myDetails.getRoomStatusObject());
            manager.beginTransaction()
                    .replace(R.id.summary_editor_container, fragment)
                    .commit();
        }
        else if(option == CANCEL_SELECTED)
        {
            //No changes. Only close DetailEditorsFragment, replace with RoomSummayFragment
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            RoomSummaryFragment fragment = new RoomSummaryFragment();
            fragment.assignData(myDetails.getRoomInformation(), myDetails.getReservationDetails(), myDetails.getRoomStatusObject());
            manager.beginTransaction()
                    .replace(R.id.summary_editor_container, fragment)
                    .commit();
        }
        else
        {
            Log.e(TAG, "Received invalid DetailsEditorFragment option.");
        }
    }

    @Override
    public void onDateChange() {
        //Only present when tablet layout. Clears the right pane to show an empty fragment.
        if (tabletLayout) {
            FragmentManager manager = getSupportFragmentManager();
            RoomSummaryFragment summaryFragment = new RoomSummaryFragment();
            manager.beginTransaction()
                    .replace(R.id.summary_editor_container, summaryFragment)
                    .commit();
        }
    }

    @Override
    public void onDateSelected(int selection, int year, int month, int day) {

        //Read fragment
        FragmentManager manager = getSupportFragmentManager();
        DetailsEditorFragment mFragment = (DetailsEditorFragment) manager.findFragmentById(R.id.summary_editor_container);

        mFragment.addNewDate(selection, year, month, day);
        //Add data
    }

    private void confirmAction(int actionToTake) {

        final int selectAction = actionToTake;
        Dialog.OnClickListener confirmActionListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                executeAction(selectAction);
            }
        };

        showConfirmationDialog(confirmActionListener, selectAction);
    }


    private void showConfirmationDialog(DialogInterface.OnClickListener listener, int selection) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (selection) {
            case CLEAN_ROOMS_ACTION:
                builder.setMessage(R.string.action_clean_rooms);
                break;
            case CHECK_OUT_ACTION:
                builder.setMessage(R.string.action_checkout_guests);
        }

        builder.setPositiveButton(R.string.confirm_action, listener);
        builder.setNegativeButton(R.string.cancel_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Processes the options from the menu: clean rooms and check-out all guests.
    private void executeAction(int action) {
        FragmentManager manager = getSupportFragmentManager();
        RoomListFragment mFragment = (RoomListFragment) manager.findFragmentById(R.id.room_list_fragment);

        if (mFragment != null) {
            mFragment.executeAction(action);
        }
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        DatabaseReference myReference = FirebaseDatabase.getInstance().getReference();
        DatabaseSupportUtilities.deleteReservation(myReference, reservation);
    }
}
