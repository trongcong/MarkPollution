package com.project.markpollution.Interfaces;

import com.project.markpollution.Objects.User;

import java.util.List;

/**
 * Created by Hung on 23-Oct-16.
 */

public interface VolleyCallback {
    void onSuccessResponse(List<User> result);
}
