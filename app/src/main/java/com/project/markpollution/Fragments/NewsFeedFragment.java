package com.project.markpollution.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.markpollution.MainActivity;
import com.project.markpollution.R;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.Fragment
 * Name project: MarkPollution
 * Date: 10/8/2016
 * Time: 1:42 AM
 */
public class NewsFeedFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fab;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (((MainActivity) getActivity()).viewPager.getCurrentItem() == 0) {
                    ((MainActivity) getActivity()).viewPager.setCurrentItem(1);
                }
                break;
        }
    }
}
