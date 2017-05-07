package itsci.mju.com.mjurescue.ListTitleAID;

import java.util.ArrayList;

/**
 * Created by Tom on 15/5/2559.
 */
public interface CallAIDListService {
    void onPreCallService();
    void onCallService();
    void onRequestCompleteListener(ArrayList<AIDBean> newsModelArrayList);
    void onRequestFailed(String result);
}
