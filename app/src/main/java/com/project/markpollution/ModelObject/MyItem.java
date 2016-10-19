package com.project.markpollution.ModelObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.ModelObject
 * Name project: MarkPollution
 * Date: 10/17/2016
 * Time: 11:24 PM
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;

    public MyItem(LatLng mPosition) {
        this.mPosition = mPosition;
    }

    @Override
    public LatLng getPosition() {
        return null;
    }
}
