package itsci.mju.com.mjurescue.ListTitleAID;

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
public class JSONCallAIDListServiceAsyncTask extends AsyncTask<String,String,String> {
    private CallAIDListService callAIDListService;
    private final String TAG = "JSONCallServiceAsynTask";

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.i(TAG,"Call Service");
            if(callAIDListService!=null){
                callAIDListService.onCallService();
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

    public JSONCallAIDListServiceAsyncTask(CallAIDListService callAIDListService) {
        this.callAIDListService = callAIDListService;
    }

    public ArrayList<AIDBean> onParserContentToModel(String dataJSon) {
        Log.e("data json", dataJSon);
        ArrayList<AIDBean> newsList = new ArrayList<AIDBean>();
        try {
            JSONObject jsonObject = new JSONObject(dataJSon);
            JSONArray jsonArray = jsonObject.optJSONArray("AIDList");
            if (jsonArray != null) {
                for (int i = 0; i<jsonArray.length(); i++) {
                    JSONObject json = jsonArray.optJSONObject(i);

                    AIDBean aidBean = new AIDBean();
                    aidBean.setAidId(json.getString("aidId"));
                    aidBean.setTitle(json.getString("title"));
                    aidBean.setDetail(json.getString("detail"));
                    aidBean.setImage(json.getString("image"));
                    newsList.add(aidBean);

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
        if (callAIDListService != null) {
            callAIDListService.onPreCallService();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (callAIDListService != null) {
            ArrayList<AIDBean> newsList = onParserContentToModel(s);
            callAIDListService.onRequestCompleteListener(newsList);
        }

    }
}
