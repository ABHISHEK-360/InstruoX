package com.appdev.abhishek360.instruo.HomeFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.appdev.abhishek360.instruo.EventTabFragments.AutomatonTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.ExhibitionsTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.GamingTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.NonGenericTabFragment;
import com.appdev.abhishek360.instruo.Adapters.EventPagerAdapter;
import com.appdev.abhishek360.instruo.EventTabFragments.EventTechnicalTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.WorkshopTabFragment;
import com.appdev.abhishek360.instruo.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class EventsFragment extends Fragment implements EventTechnicalTabFragment.OnFragmentInteractionListener, AutomatonTabFragment.OnFragmentInteractionListener
                                                            , GamingTabFragment.OnFragmentInteractionListener, ExhibitionsTabFragment.OnFragmentInteractionListener {
    private TabLayout tabs;
    private ImageView imageView;
    int tabCode=0;
    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    private StorageReference storageReference;

    private OnFragmentInteractionListener mListener;

    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        imageView=v.findViewById(R.id.events_tab_header_image);
        tabs = (TabLayout)v.findViewById(R.id.tab_layout);
        tabCode= this.getArguments().getInt("tCode");

        final ViewPager vp = (ViewPager) v.findViewById(R.id.event_pager);

        tabs.setupWithViewPager(vp);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0 :
                        storageReference=firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/technical_poster.jpeg" );
                        break;

                    case 1 :
                        storageReference=firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/automaton_poster.jpeg" );
                        break;

                    case 2 :
                        storageReference=firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/non_generic_poster.jpeg" );
                        break;

                    case 3 :
                        storageReference=firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/gaming_poster.jpeg" );
                        break;

                    case 4 :
                        storageReference=firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/technical_poster.jpeg" );
                        break;

                    case 5 :
                        storageReference=firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/gaming_poster.jpeg" );
                        break;
                }

                try {
                    Glide.with(getActivity().getApplicationContext()).using(new FirebaseImageLoader()).load(storageReference)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                }
                catch (Exception e) {
                    Log.d("Event Image:",""+e);
                    imageView.setImageResource(R.drawable.technical_poster_reduced);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp));
        SetUpViewPager(vp);

        vp.setCurrentItem(tabCode);

        return v;
    }

    private void SetUpViewPager(ViewPager viewPager) {
        EventPagerAdapter adapter = new EventPagerAdapter(getFragmentManager(),6);

        adapter.AddFragmentPage(new EventTechnicalTabFragment(),"Technical");
        adapter.AddFragmentPage(new AutomatonTabFragment(),"Automaton");
        adapter.AddFragmentPage(new NonGenericTabFragment(),"Non Generic");
        adapter.AddFragmentPage(new GamingTabFragment(),"Gaming");
        adapter.AddFragmentPage(new WorkshopTabFragment(),"Workshops");
        adapter.AddFragmentPage(new ExhibitionsTabFragment(),"Exhibitions");

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
        }
        else {
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
