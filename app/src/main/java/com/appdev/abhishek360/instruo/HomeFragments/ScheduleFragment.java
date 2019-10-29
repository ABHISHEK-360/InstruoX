package com.appdev.abhishek360.instruo.HomeFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.appdev.abhishek360.instruo.Adapters.EventPagerAdapter;
import com.appdev.abhishek360.instruo.R;
import com.appdev.abhishek360.instruo.SchedulesTabFragments.ScheduleDayOneFragment;
import com.appdev.abhishek360.instruo.SchedulesTabFragments.ScheduleDayThreeFragment;
import com.appdev.abhishek360.instruo.SchedulesTabFragments.ScheduleDayTwoFragment;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScheduleFragment extends Fragment {
    private TabLayout tabs;
    int tabCode=0;

    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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


    public void SetUpViewPager(ViewPager viewPager) {
        EventPagerAdapter adapter = new EventPagerAdapter(getFragmentManager(),3);

        adapter.AddFragmentPage(new ScheduleDayOneFragment(),"Day 1");
        adapter.AddFragmentPage(new ScheduleDayTwoFragment(),"Day 2");
        adapter.AddFragmentPage(new ScheduleDayThreeFragment(),"Day 3");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
