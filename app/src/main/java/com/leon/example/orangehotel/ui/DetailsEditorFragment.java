package com.leon.example.orangehotel.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.example.orangehotel.DataStructures.Reservation;
import com.leon.example.orangehotel.DataStructures.RoomInformation;
import com.leon.example.orangehotel.DataStructures.RoomTypes;
import com.leon.example.orangehotel.R;
import com.leon.example.orangehotel.SupportUtilities.DateUtilities;
import com.leon.example.orangehotel.SupportUtilities.WidgetIntentService;

import java.util.Date;

public class DetailsEditorFragment extends Fragment {

    //mplements DatePickerFragment.onDateSelectedInterface{

    final String TAG = "RDFragment";
    private final int ERROR_NAME = 505;
    private final int ERROR_PRICE = 507;
    private final int ERROR_DATE = 503;
    private final int DATE_ARRIVAL = 210;
    private final int DATE_DEPARTURE = 220;
    public final int CANCEL_SELECTED = 701;
    public final int SAVE_SELECTED = 702;
    private final String BUNDLE_SELECTION = "DATE_SELECTION";

    TextView roomNumberView;
    TextView roomTypeView;
    TextView nameView;
    EditText nameEditView;
    TextView priceView;
    EditText priceEditView;
    TextView checkInDateTV;
    TextView checkOutDateTV;
    Button checkInButton;
    Button checkOutButton;
    Button cancelButton;
    Button saveButton;
    Boolean hasReservation;

    //Temporary data
    String tempName;
    double tempPrice;
    String tempCheckInDateString;
    String tempCheckOutDateString;
    Date tempCheckInDate;
    Date tempCheckOutDate;
    int assignedRoom;
    String typeString;

    /*
    @Override
    public void onDateSelected(int selection, int year, int month, int day) {
        Log.v("AA", "would you look at that????");
    } */

    OnSavedConfirmed mCallback;

    public interface OnSavedConfirmed {
        void updateData(Reservation updatedReservationDetails, Boolean dateChanged, int option);
    }

    View.OnClickListener selectArrivalDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDatePickerDialog(DATE_ARRIVAL);
        }
    };

    View.OnClickListener selectDepartureDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDatePickerDialog(DATE_DEPARTURE);
        }
    };

    View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveDataToDailyDetail();
        }
    };

    View.OnClickListener cancelListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            cancelDataEdit();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (OnSavedConfirmed) context;
    }

    public DetailsEditorFragment() {
        //empty constructor
    }

    //Adds Reservation and RoomInformation to fragment.
    public void addData(RoomInformation information, Reservation reservation) {
        if (reservation != null) {

            //Temporary data
            tempName = reservation.getName();
            tempPrice = reservation.getPrice();
            tempCheckInDate = reservation.getCheckInDate();
            tempCheckOutDate = reservation.getCheckOutDate();
            tempCheckInDateString = DateUtilities.getSimpleDateFormat(tempCheckInDate);
            tempCheckOutDateString = DateUtilities.getSimpleDateFormat(tempCheckOutDate);
            assignedRoom = information.getRoomNumber();
            //typeString = RoomTypes.typeAsString(getContext(), information.getRoomType());
            hasReservation = true;

        } else {
            hasReservation = false;
            assignedRoom = information.getRoomNumber();
            //typeString = RoomTypes.typeAsString(getContext(), information.getRoomType());
            tempName = "";
            tempPrice = 0.00;
            tempCheckInDate = null;
            tempCheckOutDate = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Initialize views
        View rootView = inflater.inflate(R.layout.fragment_editor, container, false);
        roomNumberView = rootView.findViewById(R.id.room_number_view);
        roomTypeView = rootView.findViewById(R.id.room_type_view);

        nameView = rootView.findViewById(R.id.guest_name_text_view);
        nameEditView = rootView.findViewById(R.id.guest_name_edit_view);
        priceView = rootView.findViewById(R.id.price_text_view);
        priceEditView = rootView.findViewById(R.id.price_edit_view);

        checkInDateTV = rootView.findViewById(R.id.checkin_date_view);
        checkInButton = rootView.findViewById(R.id.check_in_date_button);
        checkInButton.setOnClickListener(selectArrivalDateListener);

        checkOutButton = rootView.findViewById(R.id.checkout_date_button);
        checkOutButton.setOnClickListener(selectDepartureDateListener);
        checkOutDateTV = rootView.findViewById(R.id.checkout_date_view);

        saveButton = rootView.findViewById(R.id.save_button);
        saveButton.setOnClickListener(saveListener);

        //check to see if we're in tablet layout
        cancelButton = rootView.findViewById(R.id.cancel_button);
        if(cancelButton != null)
            cancelButton.setOnClickListener(cancelListener);

        if (savedInstanceState != null) {

            tempName = savedInstanceState.getString(getString(R.string.extra_name));
            tempPrice = savedInstanceState.getDouble(getString(R.string.extra_price));
            tempCheckInDateString = savedInstanceState.getString(getString(R.string.extra_checkin_date));
            tempCheckOutDateString = savedInstanceState.getString(getString(R.string.extra_checkout_date));
            assignedRoom = savedInstanceState.getInt(getString(R.string.extra_information));
        }


        typeString = RoomTypes.typeAsString(getContext(), assignedRoom);
        //Sets views values depending on data variables.
        String assignedRoomText = getString(R.string.room_number) + " " + assignedRoom;
        roomNumberView.setText(assignedRoomText);
        roomTypeView.setText(typeString);
        nameView.setText(getString(R.string.guest_name));
        nameEditView.setText(tempName);
        priceView.setText(getString(R.string.price));
        String priceField = "" + tempPrice;
        priceEditView.setText(priceField);
        checkInDateTV.setText(tempCheckInDateString);
        checkOutDateTV.setText(tempCheckOutDateString);

        return rootView;
    }

    /* Launches DatePicker. Code differentiates between the
    Arrival date and the departure date. */
    public void showDatePickerDialog(int code) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SELECTION, code);
        newFragment.setArguments(bundle);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void saveDataToDailyDetail() {

        //Read data from screen
        tempName = nameEditView.getText().toString();
        Editable tempPriceEd = priceEditView.getText();
        String inDateString = checkInDateTV.getText().toString();
        String outDateString = checkOutDateTV.getText().toString();

        if (tempName.isEmpty()) {
            showError(ERROR_NAME);
            return;
        }

        Double price = 0.00;
        try {
            price = Double.parseDouble(tempPriceEd.toString());
        } catch (NumberFormatException exception) {
            //If unable to parse as double, exception thrown.
            showError(ERROR_PRICE);
            return;
        }

        //Price is parsed, verify it's not negative.
        //0.00 is allowed for "free" stays.
        if (price < 0.00) {
            showError(ERROR_PRICE);
            return;
        }

        /* Reads dates. If there was an existing reservation which called fragment,
        compares edited values to existing ones. If they were changed, we will update
        the Reservation and Unavailable nodes in the Firebase Real Time Database. */
        Boolean editUnavailable = false;

        if (hasReservation) {
            Date readInDate = DateUtilities.getDateObject(inDateString);
            Date readOutDate = DateUtilities.getDateObject(outDateString);

            if (readInDate.compareTo(tempCheckInDate) != 0 || readOutDate.compareTo(tempCheckOutDate) != 0) {
                editUnavailable = true;
                tempCheckInDate = DateUtilities.getDateObject(inDateString);
                tempCheckOutDate = DateUtilities.getDateObject(outDateString);
            }
        } else {
            tempCheckInDate = DateUtilities.getDateObject(inDateString);
            tempCheckOutDate = DateUtilities.getDateObject(outDateString);
            editUnavailable = false;
        }

        int comparo = tempCheckInDate.compareTo(tempCheckOutDate);
        if (comparo >= 0) {
            showError(ERROR_DATE);
            return;
        }

        /* If we have reached this point, no errors have been found. Save data.
        Return to calling activity. Update information.
         */
        Toast toast = Toast.makeText(getContext(), getString(R.string.valid_data), Toast.LENGTH_SHORT);
        toast.show();

        WidgetIntentService.startUpdateWidgetInformation(getContext());

        //send information back to Activity. Which, in turn, updates its values.
        mCallback.updateData(new Reservation(tempName, price, tempCheckInDate, tempCheckOutDate, assignedRoom), editUnavailable, SAVE_SELECTED);

    }

    private void cancelDataEdit()
    {
        mCallback.updateData(null, null, CANCEL_SELECTED);
    }

    public void showError(int error_code) {
        int duration = Toast.LENGTH_SHORT;
        String message = "";

        //Displays toast to user that information is incorrect.
        switch (error_code) {
            case ERROR_DATE:
                message = getString(R.string.error_date);
                break;
            case ERROR_PRICE:
                message = getString(R.string.error_price);
                break;
            case ERROR_NAME:
                message = getString(R.string.error_name);
        }

        Toast toast = Toast.makeText(getContext(), message, duration);
        toast.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString(getString(R.string.extra_name), tempName);
        outState.putDouble(getString(R.string.extra_price), tempPrice);
        outState.putString(getString(R.string.extra_checkin_date), tempCheckInDateString);
        outState.putString(getString(R.string.extra_checkout_date), tempCheckOutDateString);
        outState.putInt(getString(R.string.extra_information), assignedRoom);

        super.onSaveInstanceState(outState);
    }

    public void addNewDate(int selection, int year, int month, int day) {

        //Update appropriate views and values.
        switch (selection) {
            case DATE_ARRIVAL:
                tempCheckInDate = DateUtilities.getDateObject(year, month + 1, day);
                tempCheckInDateString = DateUtilities.getSimpleDateFormat(tempCheckInDate);
                checkInDateTV.setText(tempCheckInDateString);
                break;
            case DATE_DEPARTURE:
                tempCheckOutDate = DateUtilities.getDateObject(year, month + 1, day);
                tempCheckOutDateString = DateUtilities.getSimpleDateFormat(tempCheckOutDate);
                checkOutDateTV.setText(tempCheckOutDateString);
        }
    }

}
