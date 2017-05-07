package itsci.mju.com.mjurescue.ViewStudentProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import itsci.mju.com.mjurescue.R;

import static itsci.mju.com.mjurescue.UrlServlet.URL_SERVLET;


public class ViewStudentProfileFragment extends Fragment implements CallStudentProfileService{


    ArrayList<StudentBean> newsModelArrayList = null;

    private TextView txtStudentId;
    private TextView txtName;
    private TextView txtIdCard;
    private TextView phoneNumber;
    private TextView numberReserve;
    private TextView blood;
    private TextView disease;
    private TextView txtAddress;
    private TextView faculty;
    private TextView department;
    private TextView status;

    private static final String MY_PREFS = "my_prefs";



    public ViewStudentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences shared = getContext().getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
        String studentId = shared.getString("username", "not found !");
        String url_ViewStudentProfileServlet = URL_SERVLET+"/ViewStudentProfileMobileServlet?studentId="+studentId;

        new JSONCallStudentProfileServiceAsyncTask((CallStudentProfileService) this).execute(url_ViewStudentProfileServlet);
        txtStudentId = (TextView) view.findViewById(R.id.txtStudentId);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtIdCard  = (TextView) view.findViewById(R.id.txtCardID);
        phoneNumber = (TextView) view.findViewById(R.id.txtPhoneNumber);
        numberReserve = (TextView) view.findViewById(R.id.txtNumberReserve);
        blood = (TextView) view.findViewById(R.id.txtBlood);
        disease = (TextView) view.findViewById(R.id.txtDisease);
        faculty = (TextView) view.findViewById(R.id.txtFaculty);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        department = (TextView) view.findViewById(R.id.txtDeparment);
        status = (TextView) view.findViewById(R.id.txtStatus);

        return view;
    }

    @Override
    public void onPreCallService() {
        this.showProgressDialog();
    }

    @Override
    public void onCallService() {

    }

    @Override
    public void onRequestCompleteListener(ArrayList<StudentBean> newsModelArrayList) {
        if (newsModelArrayList != null && newsModelArrayList.size() > 0) {
            Log.i("Check data", "" + newsModelArrayList.size());
            this.newsModelArrayList = newsModelArrayList;
            for(int i = 0; i<newsModelArrayList.size(); i++) {
                txtStudentId.setText("รหัสนักศึกษา : " + newsModelArrayList.get(i).getStudentId());
                txtName.setText("ชื่อ-นามสกุล : "+newsModelArrayList.get(i).getStudentName());
                txtIdCard.setText("เลขบัตรประชาชน : "+newsModelArrayList.get(i).getIdCard());
                phoneNumber.setText("เบอร์โทร : "+newsModelArrayList.get(i).getPhoneNumber());
                numberReserve.setText("เบอร์โทรคนสนิท : "+newsModelArrayList.get(i).getNumberReserve());
                blood.setText("กรุ๊ปเลือด : "+newsModelArrayList.get(i).getBlood());
                disease.setText("โรคประจำตัว : "+newsModelArrayList.get(i).getDisease());
                txtAddress.setText("ที่อยู่ : "+newsModelArrayList.get(i).getAddress());
                faculty.setText("สาขาวิชา : "+newsModelArrayList.get(i).getFaculty());
                department.setText("คณะ : "+newsModelArrayList.get(i).getDepartment());
                status.setText("สถานะการใช้งาน : "+newsModelArrayList.get(i).getStatus());
            }
        }

        this.dismissProgressDialog();
    }

    @Override
    public void onRequestFailed(String result) {

    }

    public void dismissProgressDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    ProgressDialog alertDialog = null;
    public void showProgressDialog(){
        alertDialog = new ProgressDialog(getActivity());
        alertDialog.setMessage("Loading...");
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}

