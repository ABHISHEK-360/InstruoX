package com.appdev.abhishek360.instruox;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventPagerAdapter extends FragmentStatePagerAdapter
{
    int noOfTabs;
    private   List<Fragment> myFragments =new ArrayList<>();
    private   List<String> myPagerTitle =new ArrayList<>();


    public EventPagerAdapter(FragmentManager fm,int NumberOfTabs)
    {

        super(fm);
        this.noOfTabs = NumberOfTabs;
    }

    public void AddFragmentPage(Fragment frag, String title)
    {
        myFragments.add(frag);
        myPagerTitle.add(title);

    }

    @Override
    public Fragment getItem(int i)
    {


        return myFragments.get(i);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return myPagerTitle.get(position);
    }

    @Override
    public int getCount()
    {
        return noOfTabs;
    }
}
