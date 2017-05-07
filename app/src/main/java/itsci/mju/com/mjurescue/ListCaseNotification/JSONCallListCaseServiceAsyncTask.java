package itsci.mju.com.mjurescue.ListCaseNotification;

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

public class JSONCallListCaseServiceAsyncTask extends AsyncTask<String,String,String> {
    private CallListCaseService callListCaseService;
    private final String TAG = "JSONCallServiceAsynTask";

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.i(TAG,"Call Service");
            if(callListCaseService!=null){
                callListCaseService.onCallService();
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

    public JSONCallListCaseServiceAsyncTask(CallListCaseService callListCaseService) {
        this.callListCaseService = callListCaseService;
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
                    notificationBean.setNotificationID(json.getString("NotificationID"));
                    notificationBean.setStatusWorking(json.getString("statusWorking"));
                    notificationBean.setDate(json.getString("date"));
                    notificationBean.setLatitude(json.getDouble("latitude"));
                    notificationBean.setLongitude(json.getDouble("longitude"));
                    notificationBean.setRepresent(json.getString("represent"));

                    JSONObject jsonStatusCase = json.getJSONObject("formStaffBean");
                    FormStaffBean formStaffBean = new FormStaffBean();
                    formStaffBean.setStatusCase(jsonStatusCase.getString("statusCase"));
                    if(json.getJSONObject("formStaffBean").length() == 2){
                        formStaffBean.setStaffID(jsonStatusCase.getString("staffID"));
                    }

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
        if (callListCaseService != null) {
            callListCaseService.onPreCallService();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (callListCaseService != null) {
            ArrayList<NotificationBean> newsList = onParserContentToModel(s);
            callListCaseService.onRequestCompleteListener(newsList);
        }

    }
}
