package com.appdev.abhishek360.instruox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.TeamMemberViewHolder>
{
    private String[] member_pic_url;
    private String[] member_name={"Kushagra Nigam","Abhishek Kumar"};
    private String[] member_email={"kushagrawave525@gmail.com","abhi.kumar310@gmail.com"};
    private String[] member_phone={"8707427223","7979775976"};
    private String[] member_post={"Finance Head","Android App Devloper"};




    @NonNull
    @Override
    public TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());

        View view = inf.inflate(R.layout.team_view_holder,parent,false);
        return new TeamMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberViewHolder holder, int position)
    {
        holder.designation.setText(member_post[position]);
        holder.name.setText(member_name[position]);
        holder.email.setText(member_email[position]);
        holder.phone.setText(member_phone[position]);





    }

    @Override
    public int getItemCount()
    {
        return member_name.length;
    }


    public class  TeamMemberViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name,email,phone, designation;
        CircleImageView member_pic_url;

        public TeamMemberViewHolder(View itemView)
        {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.team_view_holder_name);
            email=(TextView) itemView.findViewById(R.id.team_view_holder_email);

            phone=(TextView) itemView.findViewById(R.id.team_view_holder_phone);
            designation=(TextView) itemView.findViewById(R.id.team_view_holder_post);
            member_pic_url=(CircleImageView) itemView.findViewById(R.id.team_view_holder_pic);





        }
    }

}
