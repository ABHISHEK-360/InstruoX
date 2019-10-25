package com.appdev.abhishek360.instruo.Adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.appdev.abhishek360.instruo.R;

public class ScheduleAdapter {

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

    public static class  ScheduleViewHolder extends RecyclerView.ViewHolder {
        private TextView nameScheduleTv,venueScheduleTv,timingScheduleTv;
        private CardView scheduleCard;

        public TextView getNameScheduleTv() {
            return nameScheduleTv;
        }

        public TextView getVenueScheduleTv() {
            return venueScheduleTv;
        }

        public TextView getTimingScheduleTv() {
            return timingScheduleTv;
        }

        public CardView getScheduleCard() {
            return scheduleCard;
        }

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            scheduleCard=(CardView)itemView.findViewById(R.id.shcedule_card_view);
            nameScheduleTv=(TextView) itemView.findViewById(R.id.schedule_view_holder_name);
            venueScheduleTv=(TextView) itemView.findViewById(R.id.schedule_view_holder_venue);
            timingScheduleTv=(TextView) itemView.findViewById(R.id.schedule_view_holder_timing);
        }
    }
}


