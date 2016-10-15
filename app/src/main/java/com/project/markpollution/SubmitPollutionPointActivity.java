package com.project.markpollution;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution
 * Name project: MarkPollution
 * Date: 10/11/2016
 * Time: 11:22 AM
 */

public class SubmitPollutionPointActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private EditText etTitle, etDesc;
    private Button btnSubmit;
    private ImageView ivCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_pollution_point);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapSubmit);

        mapFragment.getMapAsync(this);

        initView();
        captureOrGetFromGallery();
    }

    private void initView() {
        etTitle = (EditText) findViewById(R.id.editTextSubmitTitle);
        etDesc = (EditText) findViewById(R.id.editTextSubmitDesc);
        btnSubmit = (Button) findViewById(R.id.buttonSubmit);
        ivCamera = (ImageView) findViewById(R.id.ivCameraSubmit);
    }

    private void captureOrGetFromGallery() {
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChooseMedia();
            }
        });
    }

    private void dialogChooseMedia() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_choose_media);
        dialog.setTitle("Select option:");
        dialog.show();

        TextView tvCapture = (TextView) dialog.findViewById(R.id.textViewCapture);
        TextView tvGallery = (TextView) dialog.findViewById(R.id.textViewGallery);
        tvCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
                dialog.dismiss();
            }
        });
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePictureFromGallery();
                dialog.dismiss();
            }
        });

    }

    private void capture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 10);
    }

    private void choosePictureFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Choose picture"), 11);
    }

    private Bitmap rotateImageIfRequired(Bitmap img, Uri UriImage) {
        if(getRotation(UriImage)!=0){
            Matrix matrix = new Matrix();
            matrix.postRotate(getRotation(UriImage));
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();
            return rotatedImg;
        }else{
            return img;
        }
    }

    private int getRotation(Uri UriImage) {
        String[] filePathColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = getContentResolver().query(UriImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int rotation = 0;
        rotation = cursor.getInt(0);
        cursor.close();
        return rotation;
    }

    private String getPicturePath(Uri uriImage)
    {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uriImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }

    private Bitmap setPic(String picPath) {
        // get width and height of imageView
        int targetW = ivCamera.getWidth();
        int targetH = ivCamera.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picPath, bmOptions);
        // get width and height of photo
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(picPath, bmOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent i = getIntent();
        double lat = i.getDoubleExtra("Lat", 0);
        double lng = i.getDoubleExtra("Long", 0);

        LatLng point = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(point)
                .title("This is Pollution Point "))
                .showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // capture
        if(requestCode == 10 && resultCode == RESULT_OK){
            String picPath = getPicturePath(data.getData());
            Bitmap bm = setPic(picPath);
            Bitmap bitmap = rotateImageIfRequired(bm, data.getData());
            ivCamera.setImageBitmap(bitmap);
        }
        // choose picture from Gallery
        else if(requestCode == 11 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = rotateImageIfRequired(bm, uri);
            ivCamera.setImageBitmap(bitmap);
        }
    }
}
