package com.appdev.abhishek360.instruo.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdev.abhishek360.instruo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamMemberAdapter {

    private String name;
    private String email;
    private String phone;
    private String post;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public static class  TeamMemberViewHolder extends RecyclerView.ViewHolder {
        public TextView name_tv,email_tv, phone_tv, post_tv;
        public CircleImageView member_pic_url;

        public TeamMemberViewHolder(View itemView) {
            super(itemView);
            name_tv = (TextView) itemView.findViewById(R.id.team_view_holder_name);
            email_tv = (TextView) itemView.findViewById(R.id.team_view_holder_email);
            phone_tv = (TextView) itemView.findViewById(R.id.team_view_holder_phone);
            post_tv = (TextView) itemView.findViewById(R.id.team_view_holder_post);
            member_pic_url = (CircleImageView) itemView.findViewById(R.id.team_view_holder_pic);
        }
    }

}
