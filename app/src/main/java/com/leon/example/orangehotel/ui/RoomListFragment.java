package com.leon.example.orangehotel.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leon.example.orangehotel.DataStructures.DailyDetails;
import com.leon.example.orangehotel.DataStructures.Reservation;
import com.leon.example.orangehotel.DataStructures.RoomInformation;
import com.leon.example.orangehotel.DataStructures.RoomStatus;
import com.leon.example.orangehotel.R;
import com.leon.example.orangehotel.SupportUtilities.DailyDetailsAdapter;
import com.leon.example.orangehotel.SupportUtilities.DatabaseSupportUtilities;
import com.leon.example.orangehotel.SupportUtilities.DateUtilities;
import com.leon.example.orangehotel.SupportUtilities.WidgetProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RoomListFragment extends Fragment implements DailyDetailsAdapter.OnClickHandler {

    final String TAG = "RLF";
    private final int CLEAN_ROOMS_ACTION = 14;
    private final int CHECK_OUT_ACTION = 13;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DataSnapshot fullDataSnapshot;

    Date mDateToShow;
    Calendar mCalendar;

    private ArrayList<DailyDetails> data;
    DailyDetailsAdapter adapter;

    ImageView backArrowView;
    ImageView forwardArrowView;
    TextView todayDateView;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    Configuration config;

    OnDetailSelectedListener mCallback;

    public interface OnDetailSelectedListener {
        void onDetailSelected(DailyDetails details);
    }

    OnDateChanged layoutCallback;

    public interface OnDateChanged {
        void onDateChange();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallback = (OnDetailSelectedListener) context;
        layoutCallback = (OnDateChanged) context;
    }

    //Adds DailyDetails information to fragment.
    public void addData(ArrayList<DailyDetails> details) {
        data = details;
    }

    View.OnClickListener nextDayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            layoutCallback.onDateChange();
            //Update mDateToShow to the next day.
            mDateToShow = DateUtilities.getAdjustedDate(mDateToShow, 1);
            loadData();
        }
    };

    View.OnClickListener previousDayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //Update mDateToShow to previous day.
            layoutCallback.onDateChange();
            mDateToShow = DateUtilities.getAdjustedDate(mDateToShow, -1);
            loadData();
        }
    };

    public RoomListFragment() {
        //empty constructor.
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_daily_list, container, false);
        backArrowView = rootView.findViewById(R.id.back_arrow_view);
        forwardArrowView = rootView.findViewById(R.id.forward_arrow_view);
        todayDateView = rootView.findViewById(R.id.todays_date_view);

        backArrowView.setOnClickListener(previousDayListener);
        forwardArrowView.setOnClickListener(nextDayListener);
        config = getResources().getConfiguration();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                setFullDataSnapshot(dataSnapshot);
                pushUpdateToWidget();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        //TODO: Save instance information.
        recyclerView = rootView.findViewById(R.id.rv_daily_details);

        return rootView;
    }


    @Override
    public void onClick(DailyDetails detailsInformation) {

        mCallback.onDetailSelected(detailsInformation);
    }


    //Reads the data given back by the Firebase Real Time Database
    //Defautls to "TODAY" view
    private void setFullDataSnapshot(DataSnapshot dataSnapshot) {

        this.fullDataSnapshot = dataSnapshot;

        mCalendar = Calendar.getInstance();
        mDateToShow = mCalendar.getTime();

        loadData();

    }

    private void loadData() {
        //Extract information
        ArrayList<RoomInformation> roomInformationList =
                DatabaseSupportUtilities.readInformationData(fullDataSnapshot);
        ArrayList<RoomStatus> roomStatusList =
                DatabaseSupportUtilities.readStatusData(fullDataSnapshot);
        ArrayList<Reservation> reservationList =
                DatabaseSupportUtilities.readReservationData(fullDataSnapshot,
                        DateUtilities.generateTableHeader(mDateToShow));

        data = DailyDetails.createList(roomInformationList, reservationList, roomStatusList);
        todayDateView.setText(DateUtilities.getDateFormat(mDateToShow));

        setAdapter();
    }

    private void setAdapter() {
        adapter = new DailyDetailsAdapter(this, data, getString(R.string.rooms), getString(R.string.reservations_status));
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public void pushUpdateToWidget() {

        Context context = getContext();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));

        int reservations = DatabaseSupportUtilities.getTotalReservations(fullDataSnapshot, DateUtilities.generateTableHeader(mDateToShow));
        String[] summaryData = DatabaseSupportUtilities.readSummaryData(fullDataSnapshot, DatabaseSupportUtilities.SIMPLE_SUMMARY);

        String[] summaryDetails = new String[]{DateUtilities.getDateFormat(mDateToShow),
                "" + reservations + " " + getString(R.string.reservations) + " / " + summaryData[1] + " " + getString(R.string.rooms)};
        //Now update all widgets
        WidgetProvider.updateDailySummaryWidget(getContext(), appWidgetManager, summaryDetails, appWidgetIds);
    }

    public void executeAction(int action) {
        switch (action) {
            case CHECK_OUT_ACTION:
                //Will always try to checkout guests for 'today.' Does not depend on the actual day being shown.
                DatabaseSupportUtilities.checkOutAllGuests(fullDataSnapshot, myRef, Calendar.getInstance().getTime());
                break;
            case CLEAN_ROOMS_ACTION:
                DatabaseSupportUtilities.cleanRooms(myRef, DatabaseSupportUtilities.readStatusData(fullDataSnapshot));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(getString(R.string.extra_details), data);
    }
}