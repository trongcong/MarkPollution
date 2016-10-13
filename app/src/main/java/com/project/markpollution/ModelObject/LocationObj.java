package com.project.markpollution.ModelObject;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.ModelObject
 * Name project: MarkPollution
 * Date: 10/8/2016
 * Time: 2:37 AM
 */
public class LocationObj {
    public double Latitude;
    public double Longitude;
    String title;
    String desc;
    int id_po;
    int image;


    public LocationObj(double Latitude, double Longitude) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    public LocationObj(String title, String desc, int image, double latitude, double longitude) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        Longitude = longitude;
        Latitude = latitude;
    }

    public LocationObj(double latitude, double longitude, String title, String desc, int id_po, int image) {
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.title = title;
        this.desc = desc;
        this.id_po = id_po;
        this.image = image;
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
}
