package com.project.markpollution;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.markpollution.ModelObject.LocationObj;

public class DetailReportActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private ImageView imgDetail;
    private TextView tvTitle, tvDes;
    private RatingBar ratingBarDetail;
    private CheckBox cbSpam, cbHasResloved;
    private ListView lvComment;
    private LocationObj locationObjIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
        initView();
    }

    private void initView() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsDetail);
        imgDetail = (ImageView) findViewById(R.id.imgDetail);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDes = (TextView) findViewById(R.id.tvDes);
        ratingBarDetail = (RatingBar) findViewById(R.id.ratingBarDetail);
        lvComment = (ListView) findViewById(R.id.lvComment);

        locationObjIntent();

        tvTitle.setText(locationObjIntent.getTitle());
        tvDes.setText(locationObjIntent.getDesc());
        imgDetail.setImageResource(locationObjIntent.getImage());
        mapFragment.getMapAsync(this);
    }

    private void locationObjIntent() {
        locationObjIntent = (LocationObj) getIntent().getSerializableExtra("ArrListObj");
        if (locationObjIntent == null) {
            Toast.makeText(this, "Null...........", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Not Null......" + locationObjIntent.getLongitude()
                    + " - " + locationObjIntent.getLatitude(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng c = new LatLng(locationObjIntent.getLatitude(), locationObjIntent.getLongitude());
        mMap.addMarker(new MarkerOptions().position(c).title("This is Mark Pollution ")).showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 17));
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }


}
