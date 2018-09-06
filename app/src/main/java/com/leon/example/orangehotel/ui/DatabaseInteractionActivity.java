package com.leon.example.orangehotel.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leon.example.orangehotel.DataStructures.Reservation;
import com.leon.example.orangehotel.DataStructures.RoomInformation;
import com.leon.example.orangehotel.DataStructures.RoomStatus;
import com.leon.example.orangehotel.DataStructures.RoomTypes;
import com.leon.example.orangehotel.R;
import com.leon.example.orangehotel.SupportUtilities.DatabaseSupportUtilities;
import com.leon.example.orangehotel.SupportUtilities.DateUtilities;
import com.leon.example.orangehotel.SupportUtilities.SampleData;

import java.util.ArrayList;

public class DatabaseInteractionActivity extends AppCompatActivity {

    DatabaseReference myRef;
    DataSnapshot fullDataSnapshot;
    final String TAG = "DBIA";

    //View variables
    Button buttonOne;
    Button buttonTwo;
    Button buttonThree;
    Button buttonFour;
    Button buttonFive;
    Button buttonSix;
    Button buttonSeven;
    Button buttonEight;
    Button buttonNine;
    Button buttonTen;
    Button buttonEleven;
    Button buttonTwelve;

    //Sample Data variables
    ArrayList<RoomInformation> listThree;
    ArrayList<Reservation> listSix;
    ArrayList<RoomStatus> listRS;

    int WHICH_ROOM;

    //Erase all data
    View.OnClickListener listenerOne = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatabaseSupportUtilities.overrideAllData(myRef);
        }
    };

    //Restore to build defaults
    View.OnClickListener listenerTwo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatabaseSupportUtilities.restoreDatabase(myRef);
        }
    };

    //Add room information list
    View.OnClickListener listenerThree = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DatabaseSupportUtilities.updateRoomInformation(myRef, SampleData.getTestRoomDetailsListLong());
        }
    };
    //add or update room (ONE instance)
    View.OnClickListener listenerFour = new View.OnClickListener() {

        RoomInformation information = SampleData.getRoomInformationWithNumber(202);

        @Override
        public void onClick(View v) {
            DatabaseSupportUtilities.updateRoomInformation(myRef, information);
        }
    };
    //add reservation
    View.OnClickListener listenerFive = new View.OnClickListener() {

        Reservation reservation = SampleData.getTestReservationWithNumber(210);

        @Override
        public void onClick(View v) {
            DatabaseSupportUtilities.updateReservation(myRef, reservation);
        }
    };
    //add reservation list
    View.OnClickListener listenerSix = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DatabaseSupportUtilities.updateReservations(myRef, listSix);
        }
    };

    //test rooms with self key
    View.OnClickListener listenerSeven = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
     //       DatabaseSupportUtilities.testRoomsWithSelfKey(myRef);
            //testRoomsWithSelfKey();
        }
    };

    //read test values
    View.OnClickListener listenerEight = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatabaseSupportUtilities.deleteUnavailable(myRef, DateUtilities.getDateObject(2018, 9,5), 4, 735);
        }
    };
    //clean room
    View.OnClickListener listenerNine = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatabaseSupportUtilities.cleanRooms(myRef, SampleData.getControlledRoomStatusList());
        }
    };

    //delete reservation
    View.OnClickListener listenerTen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatabaseSupportUtilities.deleteRoomInformation(myRef, new RoomInformation(WHICH_ROOM, RoomTypes.SINGLE_BED));
            WHICH_ROOM = WHICH_ROOM + 2;
        }
    };

    View.OnClickListener listenerEleven = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DatabaseSupportUtilities.updateStatus(myRef, SampleData.getControlledRoomStatusList());
        }
    };

    View.OnClickListener listenerTwelve = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            readllPossibleData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_interaction);

        myRef = FirebaseDatabase.getInstance().getReference();
        //Initialize views
        buttonOne = findViewById(R.id.button_one);
        buttonTwo = findViewById(R.id.button_two);
        buttonThree = findViewById(R.id.button_three);
        //buttonFour = findViewById(R.id.button_four);
        //buttonFive = findViewById(R.id.button_five);
        //buttonSix = findViewById(R.id.button_six);
        //buttonSeven = findViewById(R.id.button_seven);
        buttonEight = findViewById(R.id.button_eight);
        buttonNine = findViewById(R.id.button_nine);
        buttonTen = findViewById(R.id.button_ten);
        buttonEleven = findViewById(R.id.button_eleven);
        buttonTwelve = findViewById(R.id.button_twelve);


        //set onclick listeners
        buttonOne.setOnClickListener(listenerOne);
        buttonTwo.setOnClickListener(listenerTwo);
        buttonThree.setOnClickListener(listenerThree);
        //buttonFour.setOnClickListener(listenerFour);
        //buttonFive.setOnClickListener(listenerFive);
        //buttonSix.setOnClickListener(listenerSix);
        //buttonSeven.setOnClickListener(listenerSeven);
        buttonEight.setOnClickListener(listenerEight);
        buttonNine.setOnClickListener(listenerNine);
        buttonTen.setOnClickListener(listenerTen);
        buttonEleven.setOnClickListener(listenerEleven);
        buttonTwelve.setOnClickListener(listenerTwelve);

        listThree = new ArrayList<>();
        /* listThree.add(SampleData.getRoomInformationWithNumber(200));
        listThree.add(SampleData.getRoomInformationWithNumber(205));
        listThree.add(SampleData.getRoomInformationWithNumber(304));
        listThree.add(SampleData.getRoomInformationWithNumber(315));
        listThree.add(SampleData.getRoomInformationWithNumber(308)); */
        listRS = new ArrayList<>();
        listSix = new ArrayList<>();
        //listSix = SampleData.getControlledTestReservationList();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.v(TAG, "onDataChange called.");
                setFullDataSnapshot(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        WHICH_ROOM = 730;
    }

    private void setFullDataSnapshot(DataSnapshot dataSnapshot) {

        this.fullDataSnapshot = dataSnapshot;
    }

    private void readllPossibleData() {
        listThree = DatabaseSupportUtilities.readInformationData(fullDataSnapshot);
        listRS = DatabaseSupportUtilities.readStatusData(fullDataSnapshot);
        listSix = DatabaseSupportUtilities.readReservationData(fullDataSnapshot);
        Log.v("DIA", "all information extracted from database");

    }

}
