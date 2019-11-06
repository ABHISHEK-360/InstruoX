package com.appdev.abhishek360.instruo.HomeFragments;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appdev.abhishek360.instruo.R;


public class HomeFragment extends Fragment {
    private AnimationDrawable animatedVectorDrawable;
    private ImageView animationIV;
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        animationIV = v.findViewById(R.id.home_image_view_anim);

        animatedVectorDrawable = (AnimationDrawable) animationIV.getBackground();
        animatedVectorDrawable.setEnterFadeDuration(1000);
        animatedVectorDrawable.setExitFadeDuration(2000);
        animatedVectorDrawable.start();

        return v;
    }

    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(animatedVectorDrawable!=null){
            animatedVectorDrawable.stop();
            animatedVectorDrawable = null;
        }
    }
}
