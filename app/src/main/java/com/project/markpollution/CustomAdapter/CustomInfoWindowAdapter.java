package com.project.markpollution.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.project.markpollution.ModelObject.PollutionPoint;
import com.project.markpollution.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.CustomAdapter
 * Name project: MarkPollution
 * Date: 10/8/2016
 * Time: 2:50 AM
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private ArrayList<PollutionPoint> listPo;

    public CustomInfoWindowAdapter(Context context, ArrayList<PollutionPoint> listPo) {
        this.context = context;
        this.listPo = listPo;
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
        TextView tvDes = (TextView) rootView.findViewById(R.id.tvDesc);
        ImageView iv = (ImageView) rootView.findViewById(R.id.imgMarker);
        // Getting the position from the marker
        LatLng latLng = marker.getPosition();

        for (PollutionPoint po : listPo) {
            if (po.getLat() == latLng.latitude && po.getLng() == latLng.longitude) {
                tvTitle.setText(po.getTitle());
                tvDes.setText(po.getDesc());
                new fetchPicFromUrl(iv).execute(po.getImage());
//                iv.setImageResource(po.getImage());
            }
        }
        return rootView;
    }

        private class fetchPicFromUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView iv;

        public fetchPicFromUrl(ImageView iv) {
            this.iv = iv;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = null;
            try {
                InputStream is = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            iv.setImageBitmap(bitmap);
        }
    }
}
