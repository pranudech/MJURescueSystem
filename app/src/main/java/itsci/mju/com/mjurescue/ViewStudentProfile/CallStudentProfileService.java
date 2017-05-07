package itsci.mju.com.mjurescue.ViewStudentProfile;

import java.util.ArrayList;

/**
 * Created by Tom on 15/5/2559.
 */
public interface CallStudentProfileService {
    void onPreCallService();
    void onCallService();
    void onRequestCompleteListener(ArrayList<StudentBean> newsModelArrayList);
    void onRequestFailed(String result);
}
