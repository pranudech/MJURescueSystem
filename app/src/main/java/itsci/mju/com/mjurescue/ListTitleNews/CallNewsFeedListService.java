package itsci.mju.com.mjurescue.ListTitleNews;

import java.util.ArrayList;

/**
 * Created by Tom on 15/5/2559.
 */
public interface CallNewsFeedListService {
    void onPreCallService();
    void onCallService();
    void onRequestCompleteListener(ArrayList<NewsFeedBean> newsModelArrayList);
    void onRequestFailed(String result);
}
