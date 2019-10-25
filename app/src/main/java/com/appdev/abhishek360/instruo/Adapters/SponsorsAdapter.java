package com.appdev.abhishek360.instruo.Adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdev.abhishek360.instruo.R;

public class SponsorsAdapter{
    private String cat,key,logo,name;private String purpose;

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public static class  SponsorViewHolder extends RecyclerView.ViewHolder {
        public TextView category_sponsors;
        public ImageView logo_sponsors;

        public SponsorViewHolder(View itemView) {
            super(itemView);
            category_sponsors=(TextView) itemView.findViewById(R.id.sponsors_view_holder_cat);
            logo_sponsors=(ImageView) itemView.findViewById(R.id.sponsors_logo_imageview);
        }
    }
}



