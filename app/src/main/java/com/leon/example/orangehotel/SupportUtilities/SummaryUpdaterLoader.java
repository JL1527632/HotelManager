package com.leon.example.orangehotel.SupportUtilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leon.example.orangehotel.DataStructures.RoomInformation;
import com.leon.example.orangehotel.DataStructures.RoomStatus;

import java.util.ArrayList;

public class SummaryUpdaterLoader extends AsyncTaskLoader<Void> {

    private int[] roomTotals;
    private String mHotelName;

    public SummaryUpdaterLoader(Context context, String name, int[] roomTotals)
    {
        super(context);
        mHotelName = name;
        this.roomTotals = roomTotals;
    }

    @Override
    public Void loadInBackground() {
        //Updates both Summary and hotelRooms nodes in Database.
        DatabaseReference myReference = FirebaseDatabase.getInstance().getReference();

        //Updates hotelRoomsNode
        ArrayList<RoomInformation> fullList = RoomInformation.createRoomList(roomTotals);
        DatabaseSupportUtilities.updateRoomInformation( myReference, fullList);


        ArrayList<RoomStatus> statusList = new ArrayList<>();
        for(RoomInformation k: fullList)
            statusList.add(new RoomStatus(k.getRoomNumber(), RoomStatus.CLEAN));

        DatabaseSupportUtilities.updateSummaryData(myReference, mHotelName, roomTotals);
        DatabaseSupportUtilities.updateStatus(myReference, statusList);
        return null;
    }
}
