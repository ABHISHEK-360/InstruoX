package com.appdev.abhishek360.instruox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class ScheduleFragment extends Fragment
{

    private TabLayout tabs;
    int tabCode=0;


    private OnFragmentInteractionListener mListener;

    public ScheduleFragment()
    {
        // Required empty public constructor
    }


    public static ScheduleFragment newInstance(String param1, String param2)
    {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v =inflater.inflate(R.layout.fragment_schedule, container, false);

        tabs = (TabLayout)v.findViewById(R.id.schedules_tab_layout);
        //tabCode= this.getArguments().getInt("tCode");




        final ViewPager vp = (ViewPager) v.findViewById(R.id.schedules_pager);

        tabs.setupWithViewPager(vp);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        // vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp));
        SetUpViewPager(vp);

        vp.setCurrentItem(tabCode);

        return v;
    }


    public void SetUpViewPager(ViewPager viewPager)
    {
        EventPagerAdapter adapter = new EventPagerAdapter(getFragmentManager(),3);

        adapter.AddFragmentPage(new ScheduleDayOneFragment(),"Day 1");
        adapter.AddFragmentPage(new ScheduleDayTwoFragment(),"Day 2");
        adapter.AddFragmentPage(new ScheduleDayThreeFragment(),"Day 3");




        viewPager.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
            {

                throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
