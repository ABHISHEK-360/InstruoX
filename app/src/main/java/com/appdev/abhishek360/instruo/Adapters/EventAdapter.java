package com.appdev.abhishek360.instruo.Adapters;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class EventAdapter implements Parcelable {
    private String TITLE;
    private String TIME;
    private String VENUE;
    private String DESC;
    private Map<String,String> RULES;
    private String COORDINATORS;
    private String PRIZE_MONEY;
    private String REG_FEE;

    public EventAdapter()
    {
        RULES=new HashMap<>();
    }

    protected EventAdapter(Parcel in) {
        TITLE = in.readString();
        TIME = in.readString();
        VENUE = in.readString();
        DESC = in.readString();
        RULES=new HashMap<>();
        in.readMap(RULES,null);
        COORDINATORS = in.readString();
        PRIZE_MONEY = in.readString();
        REG_FEE = in.readString();
    }

    public static final Creator<EventAdapter> CREATOR = new Creator<EventAdapter>()
    {
        @Override
        public EventAdapter createFromParcel(Parcel in)
        {
            return new EventAdapter(in);
        }

        @Override
        public EventAdapter[] newArray(int size)
        {
            return new EventAdapter[size];
        }
    };

    public void setRULES(Map<String, String> RULES) {
        this.RULES = RULES;
    }

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public Map<String, String> getRULES() {
        return RULES;
    }

    public String getCOORDINATORS() {
        return COORDINATORS;
    }

    public void setCOORDINATORS(String COORDINATORS) {
        this.COORDINATORS = COORDINATORS;
    }

    public String getPRIZE_MONEY() {
        return PRIZE_MONEY;
    }

    public void setPRIZE_MONEY(String PRIZE_MONEY) {
        this.PRIZE_MONEY = PRIZE_MONEY;
    }

    public String getREG_FEE() {
        return REG_FEE;
    }

    public void setREG_FEE(String REG_FEE) {
        this.REG_FEE = REG_FEE;
    }

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


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TITLE);
        dest.writeString(TIME);
        dest.writeString(VENUE);
        dest.writeString(DESC);
        dest.writeMap(RULES);
        dest.writeString(COORDINATORS);
        dest.writeString(PRIZE_MONEY);
        dest.writeString(REG_FEE);
    }
}
