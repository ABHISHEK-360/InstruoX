package com.appdev.abhishek360.instruo;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.TeamMemberViewHolder>
{
    private Context ctx;

    public TeamMemberAdapter(Context ctx)
    {
        this.ctx = ctx;
    }

    private int[] member_pic_url=
            {

                    R.drawable.kushagra_nigam,
                    R.drawable.abhishek_kumar,
                    R.drawable.gautam_kumar,
                    R.drawable.ankur_jha,
                    R.drawable.vishnu_deo_gupta,
                    R.drawable.amitesh_debnath,
                    R.drawable.shubham_kumar,
                    R.drawable.shashwat_jha,
                    R.drawable.vishal_vishwakarma,
                    R.drawable.sejal_singh,
                    R.drawable.ushaswini_tirunagari,
                    R.drawable.prince_kmar,
                    R.drawable.sonu_sharma,
                    R.drawable.devara_prudviraj,
                    R.drawable.puru_raj,
                    R.drawable.devang_singh,
                    R.drawable.soham_zemse,
                    R.drawable.yatish_suwalka,
                    R.drawable.jeevan_gabbar,
                    R.drawable.anurag_singh,
                    R.drawable.ravi_modi,
                    R.drawable.mayank_raj,
                    R.drawable.rock_bhavani,



            };
    private String[] member_name=
            {
                    "Kushagra Nigam",
                    "Abhishek Kumar",
                    "Gautam Kumar",
                    "Ankur Jha",
                    "Vishnu Deo Gupta",
                    "Amitesh Debnath",
                    "Shubham Kumar",
                    "Shashwat Jha",
                    "Vishal Vishwakarma",
                    "Sejal Singh",
                    "T. Ushaswini",
                    "Prince Kumar",
                    "Sonu Kumar Sharma",
                    "Devara Prudhvi Raj",
                    "Puru Raj",
                    "Devang Singh",
                    "Soham Zemse",
                    "Yatish Suwalka",
                    "Gollapalli Jeevan Venkata sai",
                    "Anurag Kumar",
                    "Ravi Kumar",
                    "Mayank Raj",
                    "Bhavani Shankar"

            };
    private String[] member_email=
            {
                    "kushagrawave525@gmail.com",
                "abhi.kumar310@gmail.com",
                "gautamkr@instruo.in",
                "aaankurjha4@gmail.com",
                "vishnu44d@gmail.com",
                "shiba.ad98@gmail.com",
                "sk.shubham1997@gmail.com",
                "shashwatjha.dd2015@cs.iiests.ac.in",
                "vishal@instruo.in",
                "sejal@instruo.in",
                "t.ushaswini@gmail.com",
                "pkprincekumar16@gmail.com",
                "sonukrshar21@gmail.com",
                "prudhvi@instruo.in",
                "puru@instruo.in",
                "10.devang@gmail.com",
                "soham@instruo.in",
                "yatish@instruo.in",
                "gjvenkatsai.1999@gmail.com",
                "agresiveanurag4one@gmail.com",
                "ravi.kr27iiest@gmail.com",
                "mayank2497@gmail.com",
                "rockbhavani8@gmail.com"
            };

    private String[] member_phone=
            {
                    "8707427223",
                    "7979775976",
                    "9903199342",
                    "7209608240",
                    "8210849023",
                    "7003323174",
                    "8240222761",
                    "8697946077",
                    "9572825609",
                    "8789085447",
                    "8017306209",
                    "7004713312",
                    "7001213187",
                    "8555884665",
                    "8757680594",
                    "8948031877",
                    "8334025944",
                    "8017387421",
                    "9182278765",
                    "7541838959",
                    "7903635648",
                    "7980749923",
                    "8583063856"
            };

    private String[] member_post=
            {
                    "Finance Head",
                    "Android App Developer",
                    "Publicity Head",
                    "Main Coordinator",
                    "Web Development and Designing",
                    "Designing Head",
                    "Publicity Head",
                    "Main Coordinator",
                    "Web Developer",
                    "Content Writer",
                    "Sponsorship Head",
                    "Event Coordinator",
                    "Sponsorship Head",
                    "Event Coordinator",
                    "Finance Head",
                    "Event Coordinator",
                    "Publicity Coordinator",
                    "Technical Coordinator",
                    "Publicity Head",
                    "Designer",
                    "Event Coordinator",
                    "Finance Head",
                    "Event Coordinator",
            };




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

        if(member_pic_url[position]!=0)
        {
            holder.member_pic_url.setImageResource(member_pic_url[position]);

            //Glide.with(ctx).load(member_pic_url[position]).into(holder.member_pic_url);
        }


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
