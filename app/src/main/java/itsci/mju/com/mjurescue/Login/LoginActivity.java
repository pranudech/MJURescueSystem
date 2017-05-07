package itsci.mju.com.mjurescue.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import itsci.mju.com.mjurescue.Activity.HomeStaffActivity;
import itsci.mju.com.mjurescue.Activity.HomeStudentActivity;
import itsci.mju.com.mjurescue.R;
import itsci.mju.com.mjurescue.Util.JSONParser;

import static itsci.mju.com.mjurescue.UrlServlet.URL_SERVLET;


public class LoginActivity extends AppCompatActivity {

    private String url_LoginServlet = URL_SERVLET+"/LoginMobileServlet";
    private JSONParser jParser = new JSONParser();
    private JSONObject json;
    private EditText txtUsername;
    private EditText txtPassword;
    private String username = "";
    private String password = "";
    private String token = "";
    private String data = "";

    private static final String MY_PREFS = "my_prefs";
    private SharedPreferences shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        token = FirebaseInstanceId.getInstance().getToken();
        shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);

    }

    public void btnOnClickSingIn(View view){

        txtUsername = (EditText) findViewById(R.id.edtStudentID);
        txtPassword = (EditText) findViewById(R.id.edtPassword);



        new LoginAsyncTask(token).execute();

        username = txtUsername.getText().toString();
        password = txtPassword.getText().toString();

        if(username.isEmpty() || password.isEmpty()){

            alertDialogLogin(username, password);
        }
    }

    private class LoginAsyncTask extends AsyncTask<String, String, String> {

        ProgressDialog asyncDialog = new ProgressDialog(LoginActivity.this);
        private String token;

        public LoginAsyncTask(String token){
            this.token = token;
        }

        @Override
        protected String doInBackground(String... args) {
            token = FirebaseInstanceId.getInstance().getToken();
            System.out.println("##### Update TOKEN to Server : " + token );

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username",username));
            params.add(new BasicNameValuePair("password",password));
            params.add(new BasicNameValuePair("token",token));
            json = jParser.makeHttpRequest(url_LoginServlet, "GET", params);

            SharedPreferences.Editor editor = shared.edit();
            try {
                data = json.getString("Login");
                Log.d("##### Read Login JSON ", json.getString("Login"));
                if(data.equals("student")){

                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putString("role", "student");
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), HomeStudentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }else if (data.equals("staff")){

                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putString("role", "staff");
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), HomeStaffActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                String check = json.getString("Login");
                if(check.equals("fail")) {
                    alertDialogLogin(username, password);
                }
                asyncDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(s);
        }
    }

    public void alertDialogLogin(String username, String password){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setIcon(R.drawable.alert);
        alertDialogBuilder.setTitle("MJU RESCUE");
        if(username.isEmpty()) {

            alertDialogBuilder.setMessage("Please input username.");

        }else if(password.isEmpty()) {
            alertDialogBuilder.setMessage("Please input password.");
        }else{
            alertDialogBuilder.setMessage("Can not Login.");
        }
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}