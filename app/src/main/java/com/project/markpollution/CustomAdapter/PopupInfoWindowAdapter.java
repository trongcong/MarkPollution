package com.project.markpollution.CustomAdapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.project.markpollution.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.CustomAdapter
 * Name project: MarkPollution
 * Date: 10/22/2016
 * Time: 11:10 PM
 */

public class PopupInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private View view = null;
    private LayoutInflater inflater = null;
    private HashMap<String, Uri> images = null;
    private Context ctxt = null;
    private int iconWidth = -1;
    private int iconHeight = -1;
    private Marker lastMarker = null;

    public PopupInfoWindowAdapter(Context ctxt, LayoutInflater inflater, HashMap<String, Uri> images) {
        this.ctxt = ctxt;
        this.inflater = inflater;
        this.images = images;

        iconWidth = ctxt.getResources().getDimensionPixelSize(R.dimen.icon_width);
        iconHeight = ctxt.getResources().getDimensionPixelSize(R.dimen.icon_height);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (view == null) {
            view = inflater.inflate(R.layout.custom_info_window, null);
        }

        if (lastMarker == null || !lastMarker.getId().equals(marker.getId())) {
            lastMarker = marker;

            TextView tv = (TextView) view.findViewById(R.id.tvTitle);

            tv.setText(marker.getTitle());
            tv = (TextView) view.findViewById(R.id.tvDesc);
            tv.setText(marker.getSnippet());

            Uri image = images.get(marker.getId());
            ImageView icon = (ImageView) view.findViewById(R.id.imgMarker);


            Picasso.with(ctxt).load(image).resize(iconWidth, iconHeight)
                    .noFade()
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .into(icon, new MarkerCallback(marker));
        }

        return (view);
    }

    static class MarkerCallback implements Callback {
        Marker marker = null;

        MarkerCallback(Marker marker) {
            this.marker = marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.showInfoWindow();
            }
        }
    }
}
