package itsci.mju.com.mjurescue.ListCaseNotification;

import java.util.ArrayList;

public interface CallListCaseService {
    void onPreCallService();
    void onCallService();
    void onRequestCompleteListener(ArrayList<NotificationBean> newsModelArrayList);
    void onRequestFailed(String result);
}
