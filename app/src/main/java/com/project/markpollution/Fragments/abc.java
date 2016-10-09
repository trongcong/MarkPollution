package com.project.markpollution.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.markpollution.R;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.Fragments
 * Name project: MarkPollution
 * Date: 10/10/2016
 * Time: 2:30 AM
 */

public class abc extends Fragment {
    public abc() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_map,container, false);
//        ImageView imgGetLocation = (ImageView) v.findViewById(R.id.imgGetLocation);
//        imgGetLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "ImageView Click", Toast.LENGTH_LONG).show();
//            }
//        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
