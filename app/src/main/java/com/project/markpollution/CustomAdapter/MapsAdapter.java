package com.project.markpollution.CustomAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.project.markpollution.ModelObject.LocationObj;
import com.project.markpollution.R;

import java.util.ArrayList;

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
    ArrayList<LocationObj> arrL;

    public MapsAdapter(Context context, ArrayList<LocationObj> arrL) {
        this.context = context;
        this.arrL = arrL;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return prepareInfoView(marker);
    }

    private View prepareInfoView(Marker marker) {
        // Getting view from the layout file custom_info_window4
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.custom_info_window, null);
        // Getting reference to the tv & iv
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        TextView tvDes = (TextView) rootView.findViewById(R.id.tvDes);
        ImageView imgMarker = (ImageView) rootView.findViewById(R.id.imgMarker);
        // Getting the position from the marker
        LatLng latLng = marker.getPosition();

        for (LocationObj l : arrL) {
            if (l.getLatitude() == latLng.latitude && l.getLongitude() == latLng.longitude) {
                tvTitle.setText(l.getTitle());
                tvDes.setText(l.getDesc());
                imgMarker.setImageResource(l.getImage());
            }
        }
        return rootView;
    }
}
