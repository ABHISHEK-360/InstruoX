package com.appdev.abhishek360.instruox;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ScheduleAdapter
{

    private String event,venue,timing;


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    static class  ScheduleViewHolder extends RecyclerView.ViewHolder
    {
        TextView name_schedule_textview,venu_schedule,timing_chedule;
        CardView scheduleCard;



        public ScheduleViewHolder(View itemView)
        {
            super(itemView);
            scheduleCard=(CardView)itemView.findViewById(R.id.shcedule_card_view);
            name_schedule_textview=(TextView) itemView.findViewById(R.id.schedule_view_holder_name);
            venu_schedule=(TextView) itemView.findViewById(R.id.schedule_view_holder_venue);


            timing_chedule=(TextView) itemView.findViewById(R.id.schedule_view_holder_timing);





        }
    }
}


