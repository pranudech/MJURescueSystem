package itsci.mju.com.mjurescue.ListRequests;

import java.util.ArrayList;


/**
 * Created by Tom on 15/5/2559.
 */
public interface CallNotificationListService {
    void onPreCallService();
    void onCallService();
    void onRequestCompleteListener(ArrayList<NotificationBean> newsModelArrayList);
    void onRequestFailed(String result);
}
