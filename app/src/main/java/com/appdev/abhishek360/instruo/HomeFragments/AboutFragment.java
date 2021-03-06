package com.appdev.abhishek360.instruo.HomeFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.abhishek360.instruo.BuildConfig;
import com.appdev.abhishek360.instruo.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class AboutFragment extends Fragment implements YouTubePlayer.OnInitializedListener {
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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
        View v=inflater.inflate(R.layout.fragment_about, container, false);

        YouTubePlayerSupportFragment youTubePlayerSupportFragment = new YouTubePlayerSupportFragment();
        youTubePlayerSupportFragment.initialize(BuildConfig.YouTubeApiKey, this);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.youtube_frame_fragment, youTubePlayerSupportFragment, "YOUTUBE_TRAILER").commit();

        /**youTubePlayerView=(YouTubePlayerView)v.findViewById(R.id.about_youtube_view);
        onInitializedListener= new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
            {
                youTubePlayerView.initialize(BuildConfig.YouTubeApiKey,onInitializedListener);
                youTubePlayer.loadPlaylist(BuildConfig.TrailerVideoCode);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };*/

        return v;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b) {
            youTubePlayer.loadVideo(BuildConfig.TrailerVideoCode);
            youTubePlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.e("YOUTUBE_PLAY_INIT_ERROR","FAILED"+youTubeInitializationResult);
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
