package com.project.markpollution.internalStaticMaps;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.internal
 * Name project: MarkPollution
 * Date: 10/11/2016
 * Time: 10:31 AM
 */

public class UrlBuilder {
    private static final String UTF8 = "UTF-8";
    private final String url;
    private StringBuilder query;

    public UrlBuilder(String url) {
        this.url = url;
    }

    public  com.project.markpollution.internalStaticMaps.UrlBuilder appendQuery(String key, Object object) {
        return appendQuery(key, object.toString());
    }

    public com.project.markpollution.internalStaticMaps.UrlBuilder appendQuery(String key, String value) {
        if(query == null) query = new StringBuilder();
        if(query.length() > 0) query.append('&');
        query.append(encode(key));
        if(value != null) query.append('=').append(encode(value));
        return this;
    }

    @Override
    public String toString() {
        if(query == null || query.length() == 0) return url;
        return url + '?' + query;
    }

    static private String encode(String text) {
        try {
            return URLEncoder.encode(text, UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
