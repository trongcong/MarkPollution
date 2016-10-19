package com.project.markpollution.ModelObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.ModelObject
 * Name project: MarkPollution
 * Date: 10/8/2016
 * Time: 2:37 AM
 */
public class LocationObj implements ClusterItem, Serializable {

    double Latitude;
    double Longitude;
    int id_po;
    String title;
    String desc;
    int image;

    LatLng mPosition;

    public LocationObj(int id_po, String title, String desc, int image, double latitude, double longitude) {
        this.id_po = id_po;
        this.title = title;
        this.desc = desc;
        this.image = image;
        Latitude = latitude;
        Longitude = longitude;
    }


    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId_po() {
        return id_po;
    }

    public void setId_po(int id_po) {
        this.id_po = id_po;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
