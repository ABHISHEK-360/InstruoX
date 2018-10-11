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


public class EventsFragment extends Fragment implements EventTechnicalTabFragment.OnFragmentInteractionListener,EventAutomatonTabFragment.OnFragmentInteractionListener
                                                            ,EventGamingTabFragment.OnFragmentInteractionListener,EventExibitionsTabFragment.OnFragmentInteractionListener


{

    private TabLayout tabs;
    int tabCode=0;



    private OnFragmentInteractionListener mListener;

    public EventsFragment()
    {
        // Required empty public constructor
    }


    public static EventsFragment newInstance(String param1, String param2)
    {
        EventsFragment fragment = new EventsFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        tabs = (TabLayout)v.findViewById(R.id.tab_layout);
        tabCode= this.getArguments().getInt("tCode");




        final ViewPager vp = (ViewPager) v.findViewById(R.id.event_pager);

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
        EventPagerAdapter adapter = new EventPagerAdapter(getFragmentManager(),6);

        adapter.AddFragmentPage(new EventTechnicalTabFragment(),"Technical");
        adapter.AddFragmentPage(new EventAutomatonTabFragment(),"Automaton");
        adapter.AddFragmentPage(new EventNonGenericTabFragment(),"Non Generic");
        adapter.AddFragmentPage(new EventGamingTabFragment(),"Gaming");
        adapter.AddFragmentPage(new EventWorkshopFragment(),"Workshops");

        adapter.AddFragmentPage(new EventExibitionsTabFragment(),"Exhibitions");

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
        } else
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

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }


    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
