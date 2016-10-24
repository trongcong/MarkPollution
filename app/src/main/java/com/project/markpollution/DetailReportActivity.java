package com.project.markpollution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.markpollution.CustomAdapter.CircleTransform;
import com.project.markpollution.CustomAdapter.CommentRecyclerViewAdapter;
import com.project.markpollution.ModelObject.Comment;
import com.project.markpollution.ModelObject.PollutionPoint;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.project.markpollution.MainActivity.listPo;

public class DetailReportActivity extends AppCompatActivity implements OnMapReadyCallback {
    private SupportMapFragment mapFragment;
    private ImageView ivPicture, ivAvatar, ivSpam, ivResolved, ivSendComment;
    private RatingBar ratingBar;
    private TextView tvTitle, tvDesc, tvRate, tvTime, tvEmail, tvCate;
    private GoogleMap gMap;
    private double lat, lng;
    private EditText etComment;
    private RecyclerView recyclerViewComment;
    private String url_InsertComment = "http://2dev4u.com/dev/markpollution/InsertComment.php";
    private String url_RetrieveCommentById = "http://2dev4u.com/dev/markpollution/RetrieveCommentById.php?id_po=";
    private String url_RetrieveUserById = "http://2dev4u.com/dev/markpollution/RetrieveUserById.php?id_user=";

    private String id_po;
    private List<Comment> listComment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        getPoInfo();

        sendComment();
        retrieveComments();
    }

    private void initView() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapDetail);
        ivPicture = (ImageView) findViewById(R.id.imageViewDetail);
        ivAvatar = (ImageView) findViewById(R.id.imageViewAvatarDetail);
        ivSpam = (ImageView) findViewById(R.id.imageViewSpam);
        ivResolved = (ImageView) findViewById(R.id.imageViewResolved);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        tvTitle = (TextView) findViewById(R.id.textViewTitleDetail);
        tvDesc = (TextView) findViewById(R.id.textViewDescDetail);
        tvRate = (TextView) findViewById(R.id.textViewRateDetail);
        tvTime = (TextView) findViewById(R.id.textViewTimeDetail);
        tvEmail = (TextView) findViewById(R.id.textViewEmailDetail);
        tvCate = (TextView) findViewById(R.id.textViewCategoryDetail);
        ivSendComment = (ImageView) findViewById(R.id.imageViewSendComment);
        etComment = (EditText) findViewById(R.id.editTextComment);
        recyclerViewComment = (RecyclerView) findViewById(R.id.recyclerViewComment);

        mapFragment.getMapAsync(this);
    }

    private void getPoInfo() {
        Intent intent = getIntent();
        String id_po = intent.getStringExtra("id_po");
        Log.i("id_po", id_po);

        String id_user = "";
        String id_cate = "";
        String title = "";
        String desc = "";
        String image = "";
        String time = "";

        for (PollutionPoint po : listPo) {
            if (po.getId().equals(id_po)) {
                id_user = po.getId_user();
                id_cate = po.getId_cate();
                lat = po.getLat();
                lng = po.getLng();
                title = po.getTitle();
                desc = po.getDesc();
                image = po.getImage();
                time = po.getTime();
                Log.i("Po Intent", "id_user " + id_user + " id_cate " + id_cate + " lat "
                        + lat + " lng " + lng + " title " + title + " desc " + desc + " image " + image + " time " + time);
                break;
            }
        }

        // pass values into Views
        tvTitle.setText(title);
        tvDesc.setText(desc);
        Picasso.with(this).load(Uri.parse(image)).placeholder(R.drawable.placeholder).into(ivPicture);
        tvTime.setText(formatDateTime(time));

        // set email & avatar of user
        setEmailnAvatar(id_user);
        // set category
        tvCate.setText(getCategoryName(Integer.parseInt(id_cate)));
    }

    private String formatDateTime(String time) {
        SimpleDateFormat originFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat resultFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");

        Date datetime = null;
        try {
            datetime = originFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultFormat.format(datetime);
    }

    private void setEmailnAvatar(String UserID) {
        String finalUrl = url_RetrieveUserById + UserID;
        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, finalUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        JSONArray arr = response.getJSONArray("response");
                        JSONObject user = arr.getJSONObject(0);
                        String email = user.getString("email");
                        String avatar = user.getString("avatar");
                        tvEmail.setText(email);
                        Picasso.with(DetailReportActivity.this).load(Uri.parse(avatar)).resize(70, 70).transform(new CircleTransform()).into(ivAvatar);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(this).add(objReq);
    }

    private String getCategoryName(int CateID) {
        String value = "";
        switch (CateID) {
            case 1:
                value = "Land Pollution";
                break;
            case 2:
                value = "Water Pollution";
                break;
            case 3:
                value = "Air Pollution";
                break;
            case 4:
                value = "Thermal Pollution";
                break;
            case 5:
                value = "Light Pollution";
                break;
            case 6:
                value = "Noise Pollution";
                break;
        }
        return value;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng point = new LatLng(lat, lng);
        gMap.addMarker(new MarkerOptions().position(point));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
        gMap.getUiSettings().setAllGesturesEnabled(false);
        gMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void sendComment() {
        ivSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etComment.getText().toString().isEmpty()) {
                    StringRequest stringReq = new StringRequest(Request.Method.POST, url_InsertComment, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(DetailReportActivity.this, response, Toast.LENGTH_SHORT).show();
                            etComment.setText(null);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DetailReportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            etComment.setText(null);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("id_po", id_po);
                            params.put("id_user", getUserID());
                            params.put("comment", etComment.getText().toString());
                            return params;
                        }
                    };

                    Volley.newRequestQueue(DetailReportActivity.this).add(stringReq);
                }
            }
        });
    }

    private String getUserID() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedpref_id_user", MODE_PRIVATE);
        return sharedPreferences.getString("sharedpref_id_user", "");
    }

    private void retrieveComments() {
        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, url_RetrieveCommentById + id_po, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        JSONArray arr = response.getJSONArray("response");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject comment = arr.getJSONObject(i);
                            listComment.add(new Comment(comment.getString("id_po"), comment.getString("id_user"), comment.getString("comment"), comment.getString("time")));
                            loadComments();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailReportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(objReq);
    }

    private void loadComments() {
//        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerViewComment.setLayoutManager(layout);
        recyclerViewComment.setAdapter(new CommentRecyclerViewAdapter(this, listComment));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
