package com.project.markpollution;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution
 * Name project: MarkPollution
 * Date: 10/11/2016
 * Time: 11:22 AM
 */

public class SubmitMarkPollutionActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private ImageView imgSelectImage;
    private TextInputEditText edTitle;
    private TextInputEditText edCategory;
    private EditText edNoiDung;
    private Button btnSubmit;

    private String userChoosenTask;
    private Uri fileUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_markpollution);
        initView();
    }

    private void initView() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapss);
        imgSelectImage = (ImageView) findViewById(R.id.imgSelectImage);
        edTitle = (TextInputEditText) findViewById(R.id.edTitle);
        edCategory = (TextInputEditText) findViewById(R.id.edCategory);
        edNoiDung = (EditText) findViewById(R.id.edNoiDung);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setEnabled(false);

        imgSelectImage.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

                break;
            case R.id.imgSelectImage:

                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent i = getIntent();
        double lat = i.getDoubleExtra("Lat", 0);
        double log = i.getDoubleExtra("Long", 0);
        Log.i("Lat long: ", lat + " - " + log);

        LatLng c = new LatLng(lat, log);
        mMap.addMarker(new MarkerOptions().position(c).title("This is Mark Pollution ")).showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 17));
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

    }


}
