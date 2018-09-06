package com.leon.example.orangehotel.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leon.example.orangehotel.DataStructures.Reservation;
import com.leon.example.orangehotel.DataStructures.RoomInformation;
import com.leon.example.orangehotel.DataStructures.RoomStatus;
import com.leon.example.orangehotel.R;
import com.leon.example.orangehotel.SupportUtilities.Data;
import com.leon.example.orangehotel.SupportUtilities.DatabaseSupportUtilities;

import java.util.ArrayList;

public class RoomSummaryActivity extends AppCompatActivity implements RoomSummaryFragment.OnChangeRequestedListener, RoomSummaryFragment.OnDeleteRequestedListener {

    final String TAG = "PTAct";

    RoomInformation mRoomInformation;
    Reservation mReservation;
    RoomStatus mRoomStatus;
    RoomSummaryFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new RoomSummaryFragment();
        Intent callingIntent = getIntent();

        if (callingIntent != null) {


            if (callingIntent.hasExtra(getString(R.string.extra_information))) {
                mRoomInformation = callingIntent.getParcelableExtra(getString(R.string.extra_information));
                mReservation = callingIntent.getParcelableExtra(getString(R.string.extra_reservation));
                mRoomStatus = callingIntent.getParcelableExtra(getString(R.string.extra_status));

                if (mReservation != null)
                    Log.v("RSA", mReservation.toLog());

            } else {

                mRoomInformation = null;
                mReservation = null;
                mRoomStatus = null;
            }
        }

        //If there is no existing data, assign information to fragment.
        if (savedInstanceState == null) {

            FragmentManager manager = getSupportFragmentManager();


            fragment.assignData(mRoomInformation, mReservation, mRoomStatus);
            manager.beginTransaction()
                    .add(R.id.room_list_fragment, fragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {

        updateViews();
        super.onResume();
    }

    @Override
    public void launchEditActivity() {

        Context context = this;
        Class destinationClass = DetailsEditorActivity.class;
        Intent intentOpenEditActivity = new Intent(context, destinationClass);

        intentOpenEditActivity.putExtra(getString(R.string.extra_information), mRoomInformation);
        intentOpenEditActivity.putExtra(getString(R.string.extra_reservation), mReservation);
        intentOpenEditActivity.putExtra(getString(R.string.extra_status), mRoomStatus);
        startActivityForResult(intentOpenEditActivity, Data.UPDATE_EDIT_RESERVATION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ArrayList<Reservation> list = new ArrayList<>();
        Reservation updatedData;

        if (requestCode == Data.UPDATE_EDIT_RESERVATION_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:

                    //reads ArrayList
                    list = data.getParcelableArrayListExtra(getString(R.string.extra_reservation));
                    mReservation = list.get(0);
                    break;
                //Make no changes.
                case RESULT_CANCELED:
                default:
            }
        }
        //else nothing.
    }

    private void updateViews() {
        FragmentManager manager = getSupportFragmentManager();
        //Refreshes information saved to Fragment.
        RoomSummaryFragment newFragment = new RoomSummaryFragment();
        newFragment.assignData(mRoomInformation, mReservation, mRoomStatus);
        manager.beginTransaction()
                .replace(R.id.room_list_fragment, newFragment)
                .commit();
    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        DatabaseReference myReference = FirebaseDatabase.getInstance().getReference();
        DatabaseSupportUtilities.deleteReservation(myReference, reservation);

        //close activity
        finish();
    }
}
