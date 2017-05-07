package itsci.mju.com.mjurescue;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import itsci.mju.com.mjurescue.Activity.HomeStaffActivity;
import itsci.mju.com.mjurescue.Activity.HomeStudentActivity;
import itsci.mju.com.mjurescue.Login.LoginActivity;
import itsci.mju.com.mjurescue.Util.JSONParser;

import static itsci.mju.com.mjurescue.UrlServlet.URL_SERVLET;

public class SplashActivity extends AppCompatActivity {


    private String url_LoginServlet = URL_SERVLET+"/LoginMobileServlet";
    private JSONParser jParser = new JSONParser();
    private JSONObject json;
    private static int SPLASH_TIME_OUT = 3000;
    private static final String MY_PREFS = "my_prefs";
    private SharedPreferences shared;
    private String username = "";
    private String password = "";
    private String role = "";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        token = FirebaseInstanceId.getInstance().getToken();
        shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        username = shared.getString("username", "not found !");
        password = shared.getString("password", "not found !");
        role = shared.getString("role", "not found !");
        Log.d("LOG_TAG", "##### Username value: " + username);
        Log.d("LOG_TAG ", "###### Password value: " + password);
        Log.d("LOG_TAG ", "###### Role value: " + role);

        if(!runtime_permissions())
            enable_login();
    }

    public void enable_login(){
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(username.equals("not found !") || password.equals("not found !") || role.equals("not found !")) {

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }else if(role.equals("student")){

                    Intent intent = new Intent(SplashActivity.this, HomeStudentActivity.class);
                    startActivity(intent);
                    finish();

                }else if(role.equals("staff")){
                    new UpDateTokenStaffToServlet(token).execute();
                    Intent intent = new Intent(SplashActivity.this, HomeStaffActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }, SPLASH_TIME_OUT);
    }

    private class UpDateTokenStaffToServlet extends AsyncTask<String, String, String> {

        private String token;

        public UpDateTokenStaffToServlet(String token) {
            this.token = token;
        }

        @Override
        protected String doInBackground(String... args) {
            token = FirebaseInstanceId.getInstance().getToken();
            System.out.println("##### SplashActivity UpDate TokenStaff To Servlet : " + token);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("token", token));
            json = jParser.makeHttpRequest(url_LoginServlet, "GET", params);

            return null;
        }
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_login();
            }else {
                runtime_permissions();
            }
        }
    }
}