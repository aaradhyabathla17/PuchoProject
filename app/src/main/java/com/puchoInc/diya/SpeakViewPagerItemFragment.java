package com.puchoInc.diya;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.puchoInc.diya.TextSpeakData.SpeakData;


public class SpeakViewPagerItemFragment extends Fragment {

    private ImageView mImage;
    private SpeakData speakData;

    public static SpeakViewPagerItemFragment getInstance(SpeakData speakdata) {
        SpeakViewPagerItemFragment fragment = new SpeakViewPagerItemFragment();

        if (speakdata != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("SpeakData", (Parcelable) speakdata);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            speakData = getArguments().getParcelable("SpeakData");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_speak_view_pager_item, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mImage = view.findViewById(R.id.image);
        init();
    }

    private void init() {
        if (speakData != null) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(getActivity())
                    .setDefaultRequestOptions(requestOptions)
                    .load(speakData.getImage())
                    .into(mImage);
        }

        //
    }
}
