package itsci.mju.com.mjurescue.RequestAssistance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import itsci.mju.com.mjurescue.GPSService.GPS_ServiceStudentNow;
import itsci.mju.com.mjurescue.R;
import itsci.mju.com.mjurescue.Util.JSONParser;

import static itsci.mju.com.mjurescue.UrlServlet.URL_SERVLET;


public class RequestAssistanceFragment extends Fragment {

    private String url_RequestAssistanceServlet = URL_SERVLET+ "/RequestAssistanceMobileServlet";
    // SharedPreferences
    private static final String MY_PREFS = "my_prefs";
    private JSONParser jParser = new JSONParser();
    private JSONObject json;
    private String status = "";
    private String studentID;
    public double longitude;
    public double latitude;
    private ImageButton btnImgGreen;
    private ImageButton btnImgYellow;
    private ImageButton btnImgRed;

    private BroadcastReceiver broadcastReceiver;

    public RequestAssistanceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assistance, container, false);

        SharedPreferences shared = getContext().getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
        studentID = shared.getString("username", "not found !");
        Log.d("LOG_TAG", "RequestAssistanceFragment = studentID value: " + studentID);

        final CheckBox checkBoxOne = (CheckBox) view.findViewById(R.id.checkBoxOne);
        final CheckBox checkBoxTwo = (CheckBox) view.findViewById(R.id.checkBoxTwo);

        TextView txtYellow = (TextView) view.findViewById(R.id.txtYellow);
        TextView txtGreen = (TextView) view.findViewById(R.id.txtGreen);
        TextView txtRed = (TextView) view.findViewById(R.id.txtRed);
        txtYellow.setText("ผู้ประสบเหตุยังมีสติอยู่");
        txtGreen.setText("\t\t\tผู้ประสบเหตุ\nอาการค้อนข้างหนัก");
        txtRed.setText("\t\t\tผู้ประสบเหตุ\n    อาการหนักมาก");

        btnImgYellow = (ImageButton) view.findViewById(R.id.btnImgYellow);
        btnImgGreen = (ImageButton) view.findViewById(R.id.btnImgGreen);
        btnImgRed = (ImageButton) view.findViewById(R.id.btnImgRed);

        btnImgYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(getActivity(), "Assistance : Yellow", Toast.LENGTH_SHORT).show();
                status = "Yellow";
                if (checkBoxOne.isChecked()) {
                    dialogFragment();
                }else{
                    new RequestAssistanceAsyncTask(status,studentID,latitude,longitude).execute();
                }
                checkBoxOne.setChecked(false);
                checkBoxTwo.setChecked(false);
                view.setClickable(false);
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        view.setClickable(true);
                    }
                }, 30000);

            }
        });

        btnImgGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(getActivity(), "Assistance : Green", Toast.LENGTH_SHORT).show();
                status = "Green";
                if (checkBoxOne.isChecked()) {
                    dialogFragment();
                }else{
                    new RequestAssistanceAsyncTask(status,studentID,latitude,longitude).execute();
                }
                checkBoxOne.setChecked(false);
                view.setClickable(false);
                new Handler().postDelayed(new Runnable()
                {
                    public void run()
                    {
                        view.setClickable(true);
                    }
                }, 30000);
            }
        });

        btnImgRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(getActivity(), "Assistance : Red", Toast.LENGTH_SHORT).show();
                status = "Red";
                if (checkBoxOne.isChecked()) {
                    dialogFragment();
                }else{
                    new RequestAssistanceAsyncTask(status,studentID,latitude,longitude).execute();
                }
                checkBoxOne.setChecked(false);
                view.setClickable(false);
                new Handler().postDelayed(new Runnable()
                {
                    public void run()
                    {
                        view.setClickable(true);
                    }
                }, 30000);
            }
        });

        return view;
    }

    public void dialogFragment(){
        FragmentManager manager = getFragmentManager();
        DialogStudentFragment dialogStudent = new DialogStudentFragment(status,latitude,longitude);
        dialogStudent.show(manager,"add keyword dialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    latitude = Double.parseDouble(""+intent.getExtras().get("getLatitude"));
                    longitude = Double.parseDouble(""+intent.getExtras().get("getLongitude"));

                }
            };

        }
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            getActivity().unregisterReceiver(broadcastReceiver);
            getActivity().stopService(new Intent(getActivity(), GPS_ServiceStudentNow.class));
        }
    }

    private class RequestAssistanceAsyncTask extends AsyncTask<String, String, String> {

        private String status;
        private String studentID;
        private Double latitude;
        private Double longitude;
        private static final String MY_PREFS = "my_prefs";

        public RequestAssistanceAsyncTask(String status,String studentID, Double latitude, Double longitude) {
            this.status = status;
            this.studentID = studentID;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected String doInBackground(String... args) {

            SharedPreferences shared = getContext().getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
            studentID = shared.getString("username", "not found !");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", FirebaseInstanceId.getInstance().getToken()));
            params.add(new BasicNameValuePair("body",status));
            params.add(new BasicNameValuePair("latitude",Double.toString(latitude)));
            params.add(new BasicNameValuePair("longitude",Double.toString(longitude)));
            params.add(new BasicNameValuePair("studentID",studentID));
            json = jParser.makeHttpRequest(url_RequestAssistanceServlet, "GET", params);

            return null;
        }

    }

}
