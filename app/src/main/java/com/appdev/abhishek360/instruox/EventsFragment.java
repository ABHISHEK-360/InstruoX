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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment implements EventTechnicalTabFragment.OnFragmentInteractionListener,EventAutomatonTabFragment.OnFragmentInteractionListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TabLayout tabs;
    int tabCode=0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

     /**   tabs.addTab(tabs.newTab().setText("Technical"));
        tabs.addTab(tabs.newTab().setText("Automaton"));
        tabs.addTab(tabs.newTab().setText("Workshops"));
        tabs.addTab(tabs.newTab().setText("Gaming"));
        tabs.addTab(tabs.newTab().setText("Exibitions"));
        tabs.addTab(tabs.newTab().setText("Shows"));


        tabs.setTabGravity(TabLayout.GRAVITY_FILL);*/


        final ViewPager vp = (ViewPager) v.findViewById(R.id.event_pager);
        //final EventPagerAdapter epAdapter = new EventPagerAdapter(getFragmentManager(),tabs.getTabCount());
       // vp.setAdapter(epAdapter);
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
        EventPagerAdapter adapter = new EventPagerAdapter(getFragmentManager(),tabs.getTabCount());
        adapter.AddFragmentPage(new EventTechnicalTabFragment(),"Technical");
        adapter.AddFragmentPage(new EventAutomatonTabFragment(),"Automaton");
        adapter.AddFragmentPage(new EventWorkshopFragment(),"Workshops");
        adapter.AddFragmentPage(new EventAutomatonTabFragment(),"Gaming");
        adapter.AddFragmentPage(new EventAutomatonTabFragment(),"Exibitions");



        viewPager.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
