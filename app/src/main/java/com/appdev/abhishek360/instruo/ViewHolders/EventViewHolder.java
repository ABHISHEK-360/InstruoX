package com.appdev.abhishek360.instruo.ViewHolders;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdev.abhishek360.instruo.R;

public class  EventViewHolder extends RecyclerView.ViewHolder {
    private TextView name_event,venue,timing;
    private Button registerEvent;
    private ImageView poster_url;
    private CardView cardView;

    public TextView getName_event() {
        return name_event;
    }

    public TextView getVenue() {
        return venue;
    }

    public TextView getTiming() {
        return timing;
    }

    public Button getRegisterEvent() {
        return registerEvent;
    }

    public ImageView getPoster_url() {
        return poster_url;
    }

    public CardView getCardView() {
        return cardView;
    }

    public EventViewHolder(View itemView) {
        super(itemView);
        name_event = (TextView) itemView.findViewById(R.id.event_view_holder_name);
        timing = (TextView) itemView.findViewById(R.id.event_view_holder_Timing);
        registerEvent = (Button)itemView.findViewById(R.id.event_view_holder_register_button);
        venue = (TextView) itemView.findViewById(R.id.event_view_holder_Venue);
        cardView = (CardView)itemView.findViewById(R.id.events_card_view);
        poster_url = (ImageView) itemView.findViewById(R.id.event_view_holder_poster);
    }
}
