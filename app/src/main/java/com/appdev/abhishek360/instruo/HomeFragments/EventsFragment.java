package com.appdev.abhishek360.instruo.HomeFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.appdev.abhishek360.instruo.EventTabFragments.AutomatonTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.ExhibitionsTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.GamingTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.NonGenericTabFragment;
import com.appdev.abhishek360.instruo.Adapters.EventPagerAdapter;
import com.appdev.abhishek360.instruo.EventTabFragments.TechnicalTabFragment;
import com.appdev.abhishek360.instruo.EventTabFragments.WorkshopTabFragment;
import com.appdev.abhishek360.instruo.LoginActivity;
import com.appdev.abhishek360.instruo.R;
import com.appdev.abhishek360.instruo.Services.AlertService;
import com.appdev.abhishek360.instruo.Services.ApiClientInstance;
import com.appdev.abhishek360.instruo.Services.ApiServices;
import com.appdev.abhishek360.instruo.Services.PreferencesManager;
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

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class EventsFragment extends Fragment {
    private TabLayout tabs;
    private ImageView imageView;
    private int tabCode = 0;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    private ApiServices apiService;
    private AlertService alertService;
    private CompositeDisposable compositeDisposable;
    private ViewPager vp;

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

        vp = (ViewPager) v.findViewById(R.id.event_pager);

        compositeDisposable = new CompositeDisposable();
        alertService = new AlertService(getActivity());
        apiService = ApiClientInstance
                .getRetrofitInstance(getActivity())
                .create(ApiServices.class);

        tabs.setupWithViewPager(vp);
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0 :
                        storageReference = firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/technical_poster.jpeg" );
                        break;

                    case 1 :
                        storageReference = firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/automaton_poster.jpeg" );
                        break;

                    case 2 :
                        storageReference = firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/non_generic_poster.jpeg" );
                        break;

                    case 3 :
                        storageReference = firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/gaming_poster.jpeg" );
                        break;

                    case 4 :
                        storageReference = firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/technical_poster.jpeg" );
                        break;

                    case 5 :
                        storageReference = firebaseStorage.getReference().child("/EVENTS_INSTRUO/APP_ASSETS/EVENT_CAT_POSTER/gaming_poster.jpeg" );
                        break;
                }

                try {
                    Glide.with(getActivity()).using(new FirebaseImageLoader()).load(storageReference)
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

        if(PreferencesManager.getPreferences(LoginActivity.spSessionId) != null)
            loadRegEvents();

        return v;
    }

    private void loadRegEvents(){
        Single<ArrayList<HashMap<String, String>>> res = apiService
                .getRegEvents();

        res.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<HashMap<String, String>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<HashMap<String, String>> res) {

                        SetUpViewPager(vp);
                        vp.setCurrentItem(tabCode);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("USER_EVENTS_API_ERROR", "Failed", e);
                    }
                });
    }

    private void SetUpViewPager(ViewPager viewPager) {
        EventPagerAdapter adapter = new EventPagerAdapter(getFragmentManager(),6);

        adapter.AddFragmentPage(new TechnicalTabFragment(),"Technical");
        adapter.AddFragmentPage(new AutomatonTabFragment(),"Automaton");
        adapter.AddFragmentPage(new NonGenericTabFragment(),"Non Generic");
        adapter.AddFragmentPage(new GamingTabFragment(),"Gaming");
        adapter.AddFragmentPage(new WorkshopTabFragment(),"Workshops");
        adapter.AddFragmentPage(new ExhibitionsTabFragment(),"Exhibitions");

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
