package com.project.markpollution.Interfaces;

import com.project.markpollution.ModelObject.User;

import java.util.List;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.project.markpollution.Interfaces
 * Name project: MarkPollution
 * Date: 10/24/2016
 * Time: 6:34 AM
 */

public interface VolleyCallback {
    void onSuccessResponse(List<User> result);
}
