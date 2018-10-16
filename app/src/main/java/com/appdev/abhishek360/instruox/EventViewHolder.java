package com.appdev.abhishek360.instruox;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class  EventViewHolder extends RecyclerView.ViewHolder
{
    TextView name_event,venue,timing;
    Button registerEvent;
    ImageView poster_url;
    CardView cardView;

    public EventViewHolder(View itemView)
    {
        super(itemView);
        name_event=(TextView) itemView.findViewById(R.id.event_view_holder_name);
        timing=(TextView) itemView.findViewById(R.id.event_view_holder_Timing);
        registerEvent=(Button)itemView.findViewById(R.id.event_view_holder_register_button);

        venue=(TextView) itemView.findViewById(R.id.event_view_holder_Venue);
        cardView=(CardView)itemView.findViewById(R.id.events_card_view);


        poster_url=(ImageView) itemView.findViewById(R.id.event_view_holder_poster);





    }
}
