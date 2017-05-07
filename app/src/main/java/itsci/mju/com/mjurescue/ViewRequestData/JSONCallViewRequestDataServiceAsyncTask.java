package itsci.mju.com.mjurescue.ViewRequestData;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Tom on 15/5/2559.
 */
public class JSONCallViewRequestDataServiceAsyncTask extends AsyncTask<String,String,String> {
    private CallViewRequestDataService callViewRequestDataService;
    private final String TAG = "JSONCallServiceAsynTask";

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.i(TAG,"Call Service");
            if(callViewRequestDataService!=null){
                callViewRequestDataService.onCallService();
            }
            return downloadContent(params[0]);
        } catch (IOException e) {
            return  "Unable to retrieve data. URL may be invalid";
        }
    }

    private String downloadContent(String myUrl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myUrl);
            Log.e("url", myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is : " + response);
            Log.e("response", "" + response);
            is = conn.getInputStream();
            Log.e("is", is.toString());
            String result = convertInputStreamToString(is);
            return result;
        }catch(Exception e) {
            Log.e("error exp", e.getMessage());
            return "error";
        }finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String convertInputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine())!=null){
            sb.append(line + "\n");
        }
        br.close();
        return sb.toString();
    }

    public JSONCallViewRequestDataServiceAsyncTask(CallViewRequestDataService callViewRequestDataService) {
        this.callViewRequestDataService = callViewRequestDataService;
    }

    public ArrayList<NotificationBean> onParserContentToModel(String dataJSon) {
        Log.e("data json", dataJSon);
        ArrayList<NotificationBean> newsList = new ArrayList<NotificationBean>();
        try {
            JSONObject jsonObject = new JSONObject(dataJSon);
            JSONArray jsonArray = jsonObject.optJSONArray("Notification");
            if (jsonArray != null) {
                for (int i = 0; i<jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);

                    NotificationBean notificationBean = new NotificationBean();
                    notificationBean.setNotificationID(json.getString("notificationID"));
                    notificationBean.setDate(json.getString("date"));
                    notificationBean.setLatitude(json.getDouble("latitude"));
                    notificationBean.setLongitude(json.getDouble("longitude"));

                    JSONObject jsonFormStaff = json.getJSONObject("formStaffBean");
                    FormStaffBean formStaffBean = new FormStaffBean();
                    formStaffBean.setStatusCase(jsonFormStaff.getString("statusCase"));
                    if(json.getJSONObject("formStaffBean").length() == 2){
                        formStaffBean.setStaffID(jsonFormStaff.getString("staffID"));
                    }

                    JSONObject jsonStudentBean = json.getJSONObject("studentBean");
                    StudentBean studentBean = new StudentBean();
                    studentBean.setStudentName(jsonStudentBean.getString("studentName"));
                    studentBean.setIdCard(jsonStudentBean.getString("idCard"));
                    studentBean.setPhoneNumber(jsonStudentBean.getString("phoneNumber"));
                    studentBean.setNumberReserve(jsonStudentBean.getString("numberReserve"));
                    studentBean.setBlood(jsonStudentBean.getString("blood"));
                    studentBean.setDisease(jsonStudentBean.getString("disease"));

                    notificationBean.setStudentBean(studentBean);
                    notificationBean.setFormStaffBean(formStaffBean);
                    newsList.add(notificationBean);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (callViewRequestDataService != null) {
            callViewRequestDataService.onPreCallService();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (callViewRequestDataService != null) {
            ArrayList<NotificationBean> newsList = onParserContentToModel(s);
            callViewRequestDataService.onRequestCompleteListener(newsList);
        }

    }
}
