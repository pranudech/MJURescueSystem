package itsci.mju.com.mjurescue.ViewRequestData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import itsci.mju.com.mjurescue.GPSService.GPS_ServiceStaffNow;
import itsci.mju.com.mjurescue.GoogleMapAPI.MapsActivity;
import itsci.mju.com.mjurescue.R;
import itsci.mju.com.mjurescue.Util.JSONParser;

import static itsci.mju.com.mjurescue.UrlServlet.URL_SERVLET;

public class ViewRequestDataActivity extends AppCompatActivity implements CallViewRequestDataService,OnMapReadyCallback {

    private String selectedId = null;
    private String url_read_json = null;
    ArrayList<NotificationBean> newsModelArrayList = null;
    private JSONParser jParser = new JSONParser();
    private JSONObject json;
    private String url_Notification = URL_SERVLET + "/ListCaseNotificationMobileServlet";

    private static final String MY_PREFS = "my_prefs";
    private SharedPreferences shared;

    private String notificationID;
    private String staffID;

    TextView textHead;
    TextView txtName;
    TextView txtPhoneNumber;
    TextView txtNumberReserve;
    TextView txtBlood;
    TextView txtDisease;
    TextView txtIDCard;
    ImageView imageStatus;
    TextView txtReceive;
    Button btnReceive;

    private GoogleMap mMap;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_data);

        Intent i = new Intent(getApplicationContext(),GPS_ServiceStaffNow.class);
        startService(i);

        Intent intent = getIntent();
        selectedId = intent.getStringExtra("selectedId");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        url_read_json = URL_SERVLET+"/ListCaseNotificationMobileServlet?notificationID="+selectedId;
        new JSONCallViewRequestDataServiceAsyncTask((CallViewRequestDataService)this).execute(url_read_json);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textHead = (TextView) findViewById(R.id.textHead);
        imageStatus = (ImageView) findViewById(R.id.imageStatus);
        txtName = (TextView) findViewById(R.id.txtName);
        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        txtNumberReserve = (TextView) findViewById(R.id.txtNumberReserve);
        txtBlood = (TextView) findViewById(R.id.txtblood);
        txtDisease = (TextView) findViewById(R.id.txtdisease);
        txtIDCard = (TextView) findViewById(R.id.txtidCard);
        txtReceive = (TextView) findViewById(R.id.txtReceive);
        btnReceive = (Button) findViewById(R.id.btnReceive);
    }

    public void onBtnClickReceive(View view){
        new UpdateNotificationToServlet(notificationID,staffID).execute();
        Toast.makeText(this, "ยืนยันสำเร็จ", Toast.LENGTH_SHORT).show();
        btnReceive.setVisibility(View.GONE);
        txtReceive.setText("** รับแจ้งเหตุแล้ว **");
    }

    public void onClickGPS(View view){
        Intent intent = new Intent(this.getApplication(), MapsActivity.class);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        startActivity(intent);
    }

    @Override
    public void onPreCallService() {

    }

    @Override
    public void onCallService() {

    }

    @Override
    public void onRequestCompleteListener(ArrayList<NotificationBean> newsModelArrayList) {
        if (newsModelArrayList != null && newsModelArrayList.size() > 0) {
            Log.i("Check data", "" + newsModelArrayList.size());
            this.newsModelArrayList = newsModelArrayList;
            for(int i = 0; i<newsModelArrayList.size(); i++) {
                notificationID = newsModelArrayList.get(i).getNotificationID();
                txtName.setText("ชื่อผู้ประสบเหตุ : "+newsModelArrayList.get(i).getStudentBean().getStudentName());
                txtPhoneNumber.setText("เบอร์โทร : "+newsModelArrayList.get(i).getStudentBean().getPhoneNumber());
                txtNumberReserve.setText("เบอร์โทรคนสนิท : "+newsModelArrayList.get(i).getStudentBean().getNumberReserve());
                txtBlood.setText("กรุ๊ปเลือด : "+newsModelArrayList.get(i).getStudentBean().getBlood());
                txtDisease.setText("โรคประจำตัว : "+newsModelArrayList.get(i).getStudentBean().getDisease());
                txtIDCard.setText("รหัสประจำตัวประชาชน : "+newsModelArrayList.get(i).getStudentBean().getIdCard());

                if(newsModelArrayList.get(i).getFormStaffBean().getStatusCase().equals("Yellow")) {
                    textHead.setTextColor(Color.parseColor("#FFCC33"));
                    imageStatus.setImageResource(R.drawable.request_yellow);
                }else if(newsModelArrayList.get(i).getFormStaffBean().getStatusCase().equals("Green")){
                    textHead.setTextColor(Color.parseColor("#009900"));
                    imageStatus.setImageResource(R.drawable.request_green);
                }else if(newsModelArrayList.get(i).getFormStaffBean().getStatusCase().equals("Red")){
                    textHead.setTextColor(Color.parseColor("#CC0000"));
                    imageStatus.setImageResource(R.drawable.request_red);
                }

                if(newsModelArrayList.get(i).getFormStaffBean().getStaffID() != null){
                    txtReceive.setText("** รับแจ้งเหตุแล้ว **");
                    btnReceive.setVisibility(View.GONE);
                }else{
                    txtReceive.setText("** ยังไม่ได้รับแจ้งเหตุ **");
                }
                txtReceive.setTextColor(Color.parseColor("#CC0000"));
            }

        }

    }

    @Override
    public void onRequestFailed(String result) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in youHere and move the camera
        LatLng youHere = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        mMap.addMarker(new MarkerOptions().position(youHere).title("Help me !!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(youHere, 15));
    }

    private class UpdateNotificationToServlet extends AsyncTask<String, String, String> {

        public UpdateNotificationToServlet(String notificationID,String staffID) {

        }

        @Override
        protected String doInBackground(String... args) {

            shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
            staffID = shared.getString("username", "not found !");
            Log.d("LOG_TAG", "UpdateNotificationToServlet username value: " + staffID);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("notificationID", notificationID));
            params.add(new BasicNameValuePair("staffID", staffID));
            json = jParser.makeHttpRequest(url_Notification, "GET", params);

            return null;
        }

    }
}


