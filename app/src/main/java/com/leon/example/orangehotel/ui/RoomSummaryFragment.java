package com.leon.example.orangehotel.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leon.example.orangehotel.DataStructures.Reservation;
import com.leon.example.orangehotel.DataStructures.RoomInformation;
import com.leon.example.orangehotel.DataStructures.RoomStatus;
import com.leon.example.orangehotel.R;

public class RoomSummaryFragment extends android.support.v4.app.Fragment {

    RoomInformation mRoomInformation;
    Reservation mReservation;
    RoomStatus mRoomStatus;

    TextView roomView;
    TextView reservationView;
    FloatingActionButton deleteFAB;
    FloatingActionButton editFAB;
    String roomInfoString;


    OnChangeRequestedListener mCallback;

    public interface OnChangeRequestedListener {
        void launchEditActivity();
    }

    OnDeleteRequestedListener mDeleteCall;

    public interface OnDeleteRequestedListener {
        void deleteReservation(Reservation reservation);
    }

    View.OnClickListener editFabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mCallback.launchEditActivity();
        }
    };

    View.OnClickListener deleteFabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDeleteCall.deleteReservation(mReservation);
        }
    };

    public RoomSummaryFragment() {
        //Empty constructor
    }

    public void assignData(RoomInformation information, Reservation reservation, RoomStatus status) {

        mRoomInformation = information;
        mReservation = reservation;
        mRoomStatus = status;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Initialize views
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        roomView = rootView.findViewById(R.id.room_information_view);
        reservationView = rootView.findViewById(R.id.reservation_view);

        editFAB = rootView.findViewById(R.id.edit_fab);
        editFAB.setOnClickListener(editFabListener);

        deleteFAB = rootView.findViewById(R.id.delete_fab);
        deleteFAB.setOnClickListener(deleteFabListener);

        if (savedInstanceState != null) {

            mRoomInformation = savedInstanceState.getParcelable(getString(R.string.extra_information));
            mReservation = savedInstanceState.getParcelable(getString(R.string.extra_reservation));
            mRoomStatus = savedInstanceState.getParcelable(getString(R.string.extra_status));
        }

        //Display information.
        if (mRoomInformation != null)
            roomInfoString = mRoomInformation.toString(getContext());
            //No room information found. Indicates tabletlayout display with no data.
            //Hides floating action button
        else {
            roomInfoString = getString(R.string.no_room_information);
            editFAB.setVisibility(View.GONE);
            reservationView.setVisibility(View.INVISIBLE);
        }

        roomView.setText(roomInfoString);

        if (mReservation != null) {
            reservationView.setText(mReservation.toString(getContext()));
        } else {
            reservationView.setText(getString(R.string.no_reservation));
            deleteFAB.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (OnChangeRequestedListener) context;
        mDeleteCall = (OnDeleteRequestedListener) context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(getString(R.string.extra_information), mRoomInformation);
        outState.putParcelable(getString(R.string.extra_reservation), mReservation);
        outState.putParcelable(getString(R.string.extra_status), mRoomStatus);
        super.onSaveInstanceState(outState);
    }


}
