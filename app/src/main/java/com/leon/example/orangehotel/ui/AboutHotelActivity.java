package com.leon.example.orangehotel.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leon.example.orangehotel.DataStructures.RoomTypes;
import com.leon.example.orangehotel.R;
import com.leon.example.orangehotel.SupportUtilities.SummaryLoader;
import com.leon.example.orangehotel.SupportUtilities.SummaryUpdaterLoader;

/*
Activity which, in theory, would require the user to fully set-up their hotel information.
 */
public class AboutHotelActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]> {

    final static int FB_SUMMARY_LOADER = 210;
    final static int FB_UPDATER_LOADER = 220;

    private final int ERROR_NAME = 501;
    private final int ERROR_ROOM = 503;

    String hotelName;
    int totalRooms;
    String totalRoomsString;
    String[] roomsTotalsText;
    int[] numTotals;

    TextView hotelNameView;
    EditText hotelNameEdit;
    TextView totalRoomsView;
    TextView singleRoomsView;
    EditText singleRoomsEdit;
    TextView singleSmokingRoomsView;
    EditText singleSmokingRoomsEdit;
    TextView doubleRoomsView;
    EditText doubleRoomsEdit;
    TextView doubleSmokingView;
    EditText doubleSmokingEdit;
    TextView suiteView;
    EditText suiteEdit;
    TextView suiteSmokingView;
    EditText suiteSmokingEdit;

    Button saveButton;

    View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (verifyData())
                sendSaveRequest();
        }
    };

    DatabaseReference mDBReference = FirebaseDatabase.getInstance().getReference();
    DataSnapshot mDataSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_hotel);

        //Initialize views
        hotelNameView = findViewById(R.id.hotel_name_view);
        hotelNameEdit = findViewById(R.id.hotel_name_edit);
        totalRoomsView = findViewById(R.id.total_rooms_view);
        singleRoomsView = findViewById(R.id.single_rooms_view);
        singleRoomsEdit = findViewById(R.id.single_rooms_edit);
        singleSmokingRoomsView = findViewById(R.id.single_smoking_view);
        singleSmokingRoomsEdit = findViewById(R.id.single_smoking_edit);
        doubleRoomsView = findViewById(R.id.double_rooms_view);
        doubleRoomsEdit = findViewById(R.id.double_rooms_edit);
        doubleSmokingView = findViewById(R.id.double_smoking_view);
        doubleSmokingEdit = findViewById(R.id.double_smoking_edit);
        suiteView = findViewById(R.id.suite_view);
        suiteEdit = findViewById(R.id.suite_edit);
        suiteSmokingView = findViewById(R.id.suite_smoking_view);
        suiteSmokingEdit = findViewById(R.id.suite_smoking_edit);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(saveListener);

        //Initialize text values for views
        hotelNameView.setText(getString(R.string.hotel_name));
        singleRoomsView.setText(RoomTypes.typeAsString(this, RoomTypes.SINGLE_BED));
        singleSmokingRoomsView.setText(RoomTypes.typeAsString(this, RoomTypes.SINGLE_SMOKING));
        doubleRoomsView.setText(RoomTypes.typeAsString(this, RoomTypes.DOUBLE_BED));
        doubleSmokingView.setText(RoomTypes.typeAsString(this, RoomTypes.DOUBLE_SMOKING));
        suiteView.setText(RoomTypes.typeAsString(this, RoomTypes.SUITE));
        suiteSmokingView.setText(RoomTypes.typeAsString(this, RoomTypes.SUITE_SMOKING));

        totalRooms = 0;
        roomsTotalsText = new String[6];
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getString(R.string.about_hotel));
        mDBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDataSnapshot = dataSnapshot;
                sendQueryRequest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {

        if (id == FB_SUMMARY_LOADER) {
            return new SummaryLoader(this, mDataSnapshot);
        } else return null;
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {


        if (data != null) {
            //Assign values
            hotelName = data[0];

            totalRooms = Integer.parseInt(data[1]);
            totalRoomsString = data[1];


            for (int k = 0; k < 6; k++) {
                roomsTotalsText[k] = data[k + 2];
            }
        } else {

            hotelName = "NAME NOT FOUND";
            totalRooms = 0;
            totalRoomsString = "0";
            roomsTotalsText = new String[]{"1", "2", "3", "4", "5", "6"};
        }

        //Update all views.
        hotelNameEdit.setText(hotelName);
        totalRoomsString = totalRoomsString + " " + getString(R.string.rooms);
        totalRoomsView.setText(totalRoomsString);

        singleRoomsEdit.setText(roomsTotalsText[RoomTypes.SINGLE_INDEX]);
        doubleRoomsEdit.setText(roomsTotalsText[RoomTypes.DOUBLE_INDEX]);
        singleSmokingRoomsEdit.setText(roomsTotalsText[RoomTypes.SINGLE_SMOKING_INDEX]);
        doubleSmokingEdit.setText(roomsTotalsText[RoomTypes.DOUBLE_SMOKING_INDEX]);
        suiteEdit.setText(roomsTotalsText[RoomTypes.SUITE_INDEX]);
        suiteSmokingEdit.setText(roomsTotalsText[RoomTypes.SUITE_SMOKING_INDEX]);
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }

    //Sends all requests to background loader which retrieve data from the database directly.
    private void sendQueryRequest() {

        /*The following section checks for internet connection.*/
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //Active connection found. Search request executes.
            getLoaderManager().restartLoader(FB_SUMMARY_LOADER, null, this).forceLoad();
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verifyData() {
        numTotals = new int[6];

        //Read Data From Screen
        hotelName = hotelNameEdit.getText().toString();
        if (hotelName.isEmpty()) {
            showError(ERROR_NAME);
            return false;
        }

        //Read room values
        numTotals[0] = Integer.parseInt(singleRoomsEdit.getText().toString());
        numTotals[1] = Integer.parseInt(doubleRoomsEdit.getText().toString());
        numTotals[2] = Integer.parseInt(singleSmokingRoomsEdit.getText().toString());
        numTotals[3] = Integer.parseInt(doubleSmokingEdit.getText().toString());
        numTotals[4] = Integer.parseInt(suiteEdit.getText().toString());
        numTotals[5] = Integer.parseInt(suiteSmokingEdit.getText().toString());

        //Check room totals are valid
        for (int n = 0; n < 6; n++) {
            if (numTotals[n] < 0) {
                showError(ERROR_ROOM);
                return false;
            }
        }

        //Otherwise, all values are valid. returns true.
        Toast toast = Toast.makeText(getBaseContext(), getString(R.string.valid_data), Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }

    private void sendSaveRequest() {
        SummaryUpdaterLoader updater = new SummaryUpdaterLoader(this, hotelName, numTotals);
        updater.loadInBackground();
    }

    private void showError(int code) {
        int duration = Toast.LENGTH_SHORT;
        String message = "";

        //Displays toast to user that information is incorrect.
        switch (code) {
            case ERROR_NAME:
                message = getString(R.string.error_name);
                break;
            case ERROR_ROOM:
                message = getString(R.string.error_total);
        }

        Toast toast = Toast.makeText(getBaseContext(), message, duration);
        toast.show();
    }
}
