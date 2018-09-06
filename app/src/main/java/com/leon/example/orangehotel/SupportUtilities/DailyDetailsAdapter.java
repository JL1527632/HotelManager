package com.leon.example.orangehotel.SupportUtilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leon.example.orangehotel.DataStructures.DailyDetails;
import com.leon.example.orangehotel.DataStructures.RoomStatus;
import com.leon.example.orangehotel.DataStructures.RoomTypes;
import com.leon.example.orangehotel.R;

import java.util.ArrayList;

/*
An adapter for DailyDetails objects.
 */
public class DailyDetailsAdapter extends RecyclerView.Adapter<DailyDetailsAdapter.ReservationViewHolder> {

    //Variable sets to 0 by default. Updated by setList method.
    private int totalRooms = 0;

    private String roomTitle;
    private String reservationStatusTitle;
    private final OnClickHandler mClickHandler;

    private ArrayList<DailyDetails> roomList;

    // The interface that receives onClick messages.
    public interface OnClickHandler {
        void onClick(DailyDetails detailsInformation);
    }

    public DailyDetailsAdapter(OnClickHandler handler, ArrayList<DailyDetails> list, String roomTitle, String reservationStatusTitle) {

        mClickHandler = handler;
        roomList = list;
        this.roomTitle = roomTitle;
        this.reservationStatusTitle = reservationStatusTitle;
        if (list != null)
            totalRooms = list.size() + 1;
    }

    public DailyDetailsAdapter(OnClickHandler handler) {
        mClickHandler = handler;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        final boolean attachToParent = false;

        View viewToProcess = inflater.inflate(R.layout.single_room_layout, parent, attachToParent);

        ReservationViewHolder holder = new ReservationViewHolder(viewToProcess);

        return holder;
    }

    @Override
    public void onBindViewHolder(ReservationViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return totalRooms;
    }


    public class ReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView roomNumberView;
        TextView descriptionView;
        LinearLayout linearLayout;

        Context context;

        public ReservationViewHolder(View viewParam) {
            super(viewParam);
            context = viewParam.getContext();

            roomNumberView = viewParam.findViewById(R.id.room_number_view);
            descriptionView = viewParam.findViewById(R.id.details_view);
            linearLayout = viewParam.findViewById(R.id.room_information_view);

            viewParam.setOnClickListener(this);
        }

        void bind(int currentItemPosition) {

            //Position zero will be the title for the list.

            if (currentItemPosition == 0) {
                roomNumberView.setText(roomTitle);
                roomNumberView.setTextColor(context.getColor(R.color.colorAccentDark));
                descriptionView.setText(reservationStatusTitle);
                descriptionView.setTextColor(context.getColor(R.color.colorAccentDark));
                linearLayout.setBackgroundResource(R.color.no_reservation_dark);
            } else {
                DailyDetails presentRoom = roomList.get(currentItemPosition - 1);

                roomNumberView.setText(presentRoom.getRoomAsText());

                /* Reads data so as to determine whether to display the room information
                or, the Reservation's name. */
                int status = presentRoom.getRoomStatus();

                if (!presentRoom.hasReservation()) {
                    String fullText = RoomTypes.typeAsString(context, presentRoom.getRoomType())
                            + ". " + RoomStatus.getStatusAsString(context, presentRoom.getRoomStatus());
                    descriptionView.setText(fullText);

                    switch (currentItemPosition % 2) {
                        case 0:
                            linearLayout.setBackgroundResource(R.color.no_reservation_dark);
                            break;
                        case 1:
                            linearLayout.setBackgroundResource(R.color.no_reservation_light);
                    }
                } else {
                    switch (currentItemPosition % 2) {
                        case 0:
                            linearLayout.setBackgroundResource(R.color.reservation_background_dark);
                            break;
                        case 1:
                            linearLayout.setBackgroundResource(R.color.reservation_background_light);
                    }
                    descriptionView.setText(presentRoom.getReservationDetails().getName());
                }

                switch (status) {
                    case RoomStatus.DIRTY:
                        descriptionView.setTextColor(context.getColor(R.color.text_dirty_room));
                        break;
                    case RoomStatus.CLEAN:
                        descriptionView.setTextColor(context.getColor(R.color.text_clean_room));
                }
            }

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            //Position 0 is title, so no click action enabled.
            if (position != 0) {
                //adjust true position to index in list.
                position--;

                DailyDetails currentDetails = roomList.get(position);
                mClickHandler.onClick(currentDetails);
            }
        }
    }
}