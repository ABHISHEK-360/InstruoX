package com.appdev.abhishek360.instruox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
