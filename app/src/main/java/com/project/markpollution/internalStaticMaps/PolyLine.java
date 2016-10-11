package com.project.markpollution.internalStaticMaps;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.internal
 * Name project: MarkPollution
 * Date: 10/11/2016
 * Time: 10:31 AM
 */

final public class PolyLine {

    private PolyLine() {}

    public static String encode(com.project.markpollution.internalStaticMaps.StaticMap.GeoPoint[] points) throws IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        int prevLat = 0, prevLng = 0;
        for (com.project.markpollution.internalStaticMaps.StaticMap.GeoPoint point: points) {
            if(!point.hasCoordinates()) return null;
            int lat = (int) (point.latitude()*1E5);
            int lng = (int) (point.longitude()*1E5);
            sb.append(encodeSigned(lat - prevLat));
            sb.append(encodeSigned(lng - prevLng));
            prevLat = lat;
            prevLng = lng;
        }
        return sb.toString();
    }

    private static StringBuilder encodeSigned(int num) {
        int sgn_num = num << 1;
        if (num < 0) sgn_num = ~sgn_num;
        return encode(sgn_num);
    }

    private static StringBuilder encode(int num) {
        StringBuilder buffer = new StringBuilder();
        while (num >= 0x20) {
            int nextValue = (0x20 | (num & 0x1f)) + 63;
            buffer.append((char) (nextValue));
            num >>= 5;
        }
        num += 63;
        buffer.append((char) (num));
        return buffer;
    }
}
