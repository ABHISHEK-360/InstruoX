package com.appdev.abhishek360.instruox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventAdapter
        //extends RecyclerView.Adapter<EventAdapter.EventsViewHolder>
{
    //private String[] member_pic_url;
    private String TITLE;
    private String TIME;
    private String VENUE;


    public String getTITLE()
    {
        return TITLE;
    }

    public void setTITLE(String TITLE)
    {
        this.TITLE = TITLE;
    }

    public String getTIME()
    {
        return TIME;
    }

    public void setTIME(String TIME)
    {
        this.TIME = TIME;
    }

    public String getVENUE()
    {
        return VENUE;
    }

    public void setVENUE(String VENUE)
    {
        this.VENUE = VENUE;
    }

     /*@NonNull
    @Override
   public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());

        View view = inf.inflate(R.layout.events_view_holder,parent,false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position)
    {
        holder.name_event.setText(TITLE);
        holder.timing.setText(TIME);
        holder.venue.setText(VENUE);






    }

    @Override
    public int getItemCount()
    {
        return 1;
    }


    public static class  EventsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name_event,venue,timing;
        ImageView poster_url;

        public EventsViewHolder(View itemView)
        {
            super(itemView);
            name_event=(TextView) itemView.findViewById(R.id.event_view_holder_name);
            timing=(TextView) itemView.findViewById(R.id.event_view_holder_Timing);

            venue=(TextView) itemView.findViewById(R.id.event_view_holder_Venue);

            poster_url=(ImageView) itemView.findViewById(R.id.event_view_holder_poster);





        }
    }*/

}
