package com.project.markpollution.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.project.markpollution.Fragments.MapsFragment;
import com.project.markpollution.R;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.CustomAdapter
 * Name project: MarkPollution
 * Date: 10/8/2016
 * Time: 2:50 AM
 */
public class MapsAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;


    public MapsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file custom_info_window4
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_info_window, null);
        // Getting the position from the marker
        LatLng latLng = marker.getPosition();
        // Getting reference to the TextView to set latitude
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        // Getting reference to the TextView to set longitude
        TextView tvDes = (TextView) v.findViewById(R.id.tvDes);

        ImageView imgMaker = (ImageView) v.findViewById(R.id.imgMaker);
//        for (LocationObj l : MapsFragment.list) {
//            imgMaker.setImageResource(l.getImage());
//            tvTitle.setText(l.getTitle());
//            tvDes.setText(l.getDesc());
//        }
        for (int i = 0; i < MapsFragment.list.size(); i++) {
            imgMaker.setImageResource(MapsFragment.list.get(i).getImage());
            tvTitle.setText(MapsFragment.list.get(i).getTitle());
            tvDes.setText(MapsFragment.list.get(i).getDesc());
        }
//        tvTitle.setText(latLng.latitude+"");
//        tvDes.setText(latLng.longitude+"");

        return v;
    }
}
