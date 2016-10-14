package com.project.markpollution.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.markpollution.CustomAdapter.MapsAdapter;
import com.project.markpollution.ModelObject.LocationObj;
import com.project.markpollution.R;
import com.project.markpollution.SubmitMarkPollutionActivity;

import java.util.ArrayList;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.Fragments
 * Name project: MarkPollution
 * Date: 10/8/2016
 * Time: 1:47 AM
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {

    public ArrayList<LocationObj> listL;

    Context mContext;
    ImageView imgGetLocation;
    SupportMapFragment mapFragment;
    private FloatingActionButton fabCheck;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    public MapsFragment() {
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Fragment Demo", "Fragment_Maps onCreate()");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        mContext = getActivity();
        initView(rootView);



        if (checkPlayServices()) {
            if (!isLocationEnabled(mContext)) {
                openDiaLogCheckGPS();
                buildGoogleApiClient();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }
        demoData();
        return rootView;
    }

    private void initView(View rootView) {
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        fabCheck = (FloatingActionButton) rootView.findViewById(R.id.fabCheck);
        imgGetLocation = (ImageView) rootView.findViewById(R.id.imgGetLocation);

        imgGetLocation.setOnClickListener(this);
        mapFragment.getMapAsync(this);
        fabCheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCheck:
                Intent i = new Intent(mContext, SubmitMarkPollutionActivity.class);
                i.putExtra("Lat", mMap.getCameraPosition().target.latitude);
                i.putExtra("Long", mMap.getCameraPosition().target.longitude);
                startActivity(i);
                break;
            case R.id.imgGetLocation:
                Toast.makeText(getContext(), mMap.getCameraPosition().target.latitude
                        + " - " + mMap.getCameraPosition().target.longitude, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (LocationObj l : listL) {
            setMaket(l);
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "Ứng dụng chưa cấp quyền tìm vị trí !!!", Toast.LENGTH_LONG).show();
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnCameraIdleListener(this);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    void setMaket(LocationObj obj) {
        LatLng sydney = new LatLng(obj.getLatitude(), obj.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney));
        MapsAdapter mapsAdapter = new MapsAdapter(mContext, listL);
        mMap.setInfoWindowAdapter(mapsAdapter);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));

    }

    void demoData() {
        listL = new ArrayList<>();
        listL.add(new LocationObj("Title 1", "Description 1", R.drawable.admin, 21.027763544534345, 105.834158398211));
        listL.add(new LocationObj("Title 2", "Description 2", R.drawable.add_marker, 21.027489088033935, 105.83488393574953));
        listL.add(new LocationObj("Title 3", "Description 3", R.drawable.background_material, 21.027214005127888, 105.83561684936285));
        listL.add(new LocationObj("Title 4", "Description 4", R.drawable.logout, 21.027931910985746, 105.83607684820892));
        listL.add(new LocationObj("Title 5", "Description 5", R.drawable.ic_email_black, 21.02871709699915, 105.83497546613216));
    }

    // Gọi khi người dùng có kết nối từ GoogleApiClient
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "Ứng dụng chưa cấp quyền tìm vị trí !!!", Toast.LENGTH_LONG).show();
            return;
        }
        // Xác định vị trí cuối cùng được biết đến của thiết bị người dùng.
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            changeMap(mLastLocation);
            Log.d("LOG", "ON connected");
        } else
            try {
                // Loại bỏ tất cả các bản cập nhật vị trí cho mục đích chờ nhất định.
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            // Yêu cầu cập nhật vị trí
            LocationRequest mLocationRequest = new LocationRequest();
            // Thời gian cập nhật vị trí
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            //Độ chính xác của vị trí
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Gọi khi người dùng tạm thời trong tình trạng ngắt kết nối .
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Log", "Connection suspended");
        mGoogleApiClient.connect();
    }

    // Gọi khi có lỗi kết nối giữa client to service.
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOG", "Connection Failed");
    }

    // Gọi khi vị trí đã được thay đổi
    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                changeMap(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // synchronized: đồng bộ hóa, -- gọi nhiều nơi, cùng lúc (google)
    protected synchronized void buildGoogleApiClient() {
        // Khởi tạo, cấu hình một GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    // Gọi khi ứng dụng khởi động
    @Override
    public void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gọi khi ứng dụng tắt,
    @Override
    public void onStop() {
        super.onStop();
        try {
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    // Xác minh rằng các dịch vụ Google Play có sẵn và đã update trên thiết bị này chưa
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), 9000).show();
            } else {
//                finish();
            }
            return false;
        }
        return true;
    }

    // Di chuyển Camera vị trí của người dùng
    private void changeMap(Location location) {
        Log.d("LOG", "Reaching map" + mMap);
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "Ứng dụng chưa cấp quyền tìm vị trí !!!", Toast.LENGTH_LONG).show();
            return;
        }
        // check if map is created successfully or not
        if (mMap != null) {
            LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLong).zoom(17f).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            Toast.makeText(mContext, location.getLatitude() + " - " + location.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }

    // Lấy vị trí Camera --> Giữa bản đồ
    @Override
    public void onCameraIdle() {
        Log.d("Lat Long: ", "Idle Lat: " + mMap.getCameraPosition().target.latitude
                + " Idle Long: " + mMap.getCameraPosition().target.longitude
        );
    }


    // Dialog yêu cầu kích hoạt GPS
    private void openDiaLogCheckGPS() {
        LayoutInflater inflater = getLayoutInflater(getArguments());
        View alertLayout = inflater.inflate(R.layout.custom_dialog_check_gps, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(alertLayout);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("OKAY!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
}
