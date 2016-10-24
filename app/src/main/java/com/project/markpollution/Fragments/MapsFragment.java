package com.project.markpollution.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.markpollution.CustomAdapter.PopupInfoWindowAdapter;
import com.project.markpollution.DetailReportActivity;
import com.project.markpollution.ModelObject.Category;
import com.project.markpollution.ModelObject.PollutionPoint;
import com.project.markpollution.R;
import com.project.markpollution.SendReportActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.project.markpollution.MainActivity.listPo;
import static com.project.markpollution.R.id.map;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.Fragments
 * Name project: MarkPollution
 * Date: 10/8/2016
 * Time: 1:47 AM
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener {

    public static ArrayList<PollutionPoint> listPoByCateID;
    Context mContext;
    ImageView imgGetLocation;
    SupportMapFragment mapFragment;
    Spinner spnPhanLoai;
    String id_cate = "0";    // to store id's category on selected item (In spinner)
    private FloatingActionButton fabCheck, fabCheckSelect;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, Uri> images = new HashMap<>();
    private String url_retrieve_cate = "http://2dev4u.com/dev/markpollution/RetrieveCategory.php";
    private String url_RetrievePollutionByCateID =
            "http://dev.2dev4u.com/markpollution/RetrievePollutionBy_CateID.php?id_cate=";
    private ArrayList<Category> listCate;
    // Progress dialog
    private ProgressDialog pDialog;

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
        initView(rootView);

        loadSpinner();
        Log.d("id_cate_onCreateView", id_cate + " onCreateView");
        returnIDCate();
        getPollutionByCateID(id_cate);
        return rootView;
    }

    private void initView(View rootView) {
        mContext = getActivity();
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(map);
        fabCheck = (FloatingActionButton) rootView.findViewById(R.id.fabCheck);
        fabCheckSelect = (FloatingActionButton) rootView.findViewById(R.id.fabCheckSelect);
        imgGetLocation = (ImageView) rootView.findViewById(R.id.imgGetLocation);
        spnPhanLoai = (Spinner) rootView.findViewById(R.id.spnPhanLoai);

        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        if (checkPlayServices()) {
            if (!isLocationEnabled(mContext)) {
                openDiaLogCheckGPS();
                buildGoogleApiClient();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }

        imgGetLocation.setOnClickListener(this);
        mapFragment.getMapAsync(this);
        fabCheck.setOnClickListener(this);
        fabCheckSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCheck:
                imgGetLocation.setVisibility(View.VISIBLE);
                fabCheckSelect.setVisibility(View.VISIBLE);
                fabCheck.setVisibility(View.GONE);
                break;
            case R.id.fabCheckSelect:
                Intent i = new Intent(mContext, SendReportActivity.class);
                i.putExtra("Lat", mMap.getCameraPosition().target.latitude);
                i.putExtra("Long", mMap.getCameraPosition().target.longitude);
                startActivity(i);
                break;
            case R.id.imgGetLocation:
//                Toast.makeText(getContext(), mMap.getCameraPosition().target.latitude
//                        + " - " + mMap.getCameraPosition().target.longitude, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // extract markers from List<> into Google Map
        for (PollutionPoint po : listPo) {
            addMarker(mMap, po);
        }

        mMap.setInfoWindowAdapter(new PopupInfoWindowAdapter(getContext(),
                LayoutInflater.from(getContext()), images));

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "Ứng dụng chưa cấp quyền tìm vị trí !!!", Toast.LENGTH_LONG).show();
            return;
        }
        mMap.setOnInfoWindowClickListener(this);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    // When Click inFoWindows
    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getActivity(), DetailReportActivity.class);
        intent.putExtra("id_po", marker.getTag().toString());
        startActivity(intent);
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
//                changeMap(location);
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // synchronized: đồng bộ hóa, -- gọi nhiều nơi, cùng lúc (google)
    protected synchronized void buildGoogleApiClient() {
        // Khởi tạo, cấu hình một GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
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
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong)
                    .zoom(15f)
                    .build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            Toast.makeText(mContext, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }


    // Dialog yêu cầu kích hoạt GPS
    private void openDiaLogCheckGPS() {
        LayoutInflater inflater = getLayoutInflater(getArguments());
        View alertLayout = inflater.inflate(R.layout.custom_dialog_check_gps, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(alertLayout);
        builder.setNegativeButton("CANCEL", null);
        builder.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void addMarker(GoogleMap map, PollutionPoint po) {
        Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(po.getLat(), po.getLng()))
                .title(po.getTitle())
                .snippet(po.getDesc()));
        marker.setTag(po.getId());

        images.put(marker.getId(), Uri.parse(po.getImage()));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
    }


    private void loadSpinner() {
        StringRequest stringReq = new StringRequest(Request.Method.GET,
                url_retrieve_cate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray arr = jObj.getJSONArray("result");
                    listCate = new ArrayList<>();
                    listCate.add(0, new Category("0", "Show All"));
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject cate = arr.getJSONObject(i);
                        listCate.add(new Category(cate.getString("id_cate"), cate.getString("name_cate")));
                    }
//                    listCate.add(arr.length(),new Category("0","Reset"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<Category> adapter = new ArrayAdapter<>(mContext,
                        android.R.layout.simple_spinner_item, listCate);
                // Drop down layout style - list view with radio button
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                // attaching data adapter to spinner
                spnPhanLoai.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(mContext).add(stringReq);
    }

    private void getPollutionByCateID(String CateID) {
        String finalUrl = url_RetrievePollutionByCateID + CateID;
        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, finalUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("result");
                            Log.i("response", response.toString());
                            Log.i("arr.length()", arr.length() + "");
                            listPoByCateID = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject po = arr.getJSONObject(i);
                                String id_po = po.getString("id_po");
                                String id_cate = po.getString("id_cate");
                                String id_user = po.getString("id_user");
                                double lat = po.getDouble("lat");
                                double lng = po.getDouble("lng");
                                String title = po.getString("title");
                                String desc = po.getString("desc");
                                String image = po.getString("image");
                                String time = po.getString("time");

                                listPoByCateID.add(new PollutionPoint(id_po, id_cate,
                                        id_user, lat, lng, title, desc, image, time));
                            }
                            for (PollutionPoint po : listPoByCateID) {
                                addMarker(mMap, po);
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(new LatLng(po.getLat(), po.getLng()), 12));
                            }
                            Log.d("listPoByCateID", listPoByCateID.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(mContext).add(objReq);
    }

    private void returnIDCate() {
        if (spnPhanLoai.getSelectedItemPosition() == 0) {
            id_cate = "0";
        }
        if (spnPhanLoai.getSelectedItemPosition() == 1) {
            id_cate = "1";
        }
        if (spnPhanLoai.getSelectedItemPosition() == 2) {
            id_cate = "2";
        }
        if (spnPhanLoai.getSelectedItemPosition() == 3) {
            id_cate = "3";
        }
        if (spnPhanLoai.getSelectedItemPosition() == 4) {
            id_cate = "4";
        }
        if (spnPhanLoai.getSelectedItemPosition() == 5) {
            id_cate = "5";
        }
        if (spnPhanLoai.getSelectedItemPosition() == 6) {
            id_cate = "6";
        }

        spnPhanLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Category cate = (Category) spnPhanLoai.getItemAtPosition(i);
                id_cate = cate.getId();
                switch (i) {
                    case 0:
                        for (PollutionPoint po : listPo) {
                            addMarker(mMap, po);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(po.getLat(), po.getLng()), 12));
                        }
                        break;
                    case 1:
                        Log.d("id_cate_switchcase", id_cate);
                        mMap.clear();
                        showpDialog();
                        getPollutionByCateID(id_cate);
                        hidepDialog();
                        break;
                    case 2:
                        Log.d("id_cate_switchcase", id_cate);
                        mMap.clear();
                        showpDialog();
                        getPollutionByCateID(id_cate);
                        hidepDialog();
                        break;
                    case 3:
                        Log.d("id_cate_switchcase", id_cate);
                        mMap.clear();
                        showpDialog();
                        getPollutionByCateID(id_cate);
                        hidepDialog();
                        break;
                    case 4:
                        Log.d("id_cate_switchcase", id_cate);
                        mMap.clear();
                        showpDialog();
                        getPollutionByCateID(id_cate);
                        hidepDialog();
                        break;
                    case 5:
                        Log.d("id_cate_switchcase", id_cate);
                        mMap.clear();
                        showpDialog();
                        getPollutionByCateID(id_cate);
                        hidepDialog();
                        break;
                    case 6:
                        Log.d("id_cate_switchcase", id_cate);
                        mMap.clear();
                        showpDialog();
                        getPollutionByCateID(id_cate);
                        hidepDialog();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
