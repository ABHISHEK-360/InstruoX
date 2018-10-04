package com.appdev.abhishek360.instruox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventsViewHolder>
{
    private String[] member_pic_url;
    private String[] event_name={"Events List 1","Events List 1"};
    private String[] event_timing={"To be updated","To be updated"};
    private String[] event_venue={"TBD","TBD"};





    @NonNull
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
        holder.name_event.setText(event_name[position]);
        holder.timing.setText(event_timing[position]);
        holder.venue.setText(event_venue[position]);






    }

    @Override
    public int getItemCount()
    {
        return event_name.length;
    }


    public class  EventsViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name_event,venue,timing;
        ImageView poster_url;

        public EventsViewHolder(View itemView)
        {
            super(itemView);
            name_event=(TextView) itemView.findViewById(R.id.event_view_holder_name);
            timing=(TextView) itemView.findViewById(R.id.event_view_holder_Timing);

            venue=(TextView) itemView.findViewById(R.id.event_view_holder_Venue);

            poster_url=(ImageView) itemView.findViewById(R.id.event_view_holder_poster);





        }
    }

}
