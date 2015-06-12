package com.example.lawrence.chatingroomdemo.messaging_agent;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lawrence on 12/6/15.
 */
public class ResponseData {

    protected int responseCode;
    protected String responseText;

    public ResponseData(JSONObject responseJsonData) {

        try {
            responseCode = responseJsonData.getInt("code");
            responseText = responseJsonData.getString("text");
//        String responseUrl = responseJsonData.getString("url");
//        JSONArray responseListContent = responseJsonData.getJSONArray("list");

            Log.d("Response Code ", String.valueOf(responseCode));
            Log.d("Response Text", responseText);
//        Log.d("Response Url", responseUrl);
//        Log.d("Response List", responseText);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getResponseText() {
        return responseText;
    }
}
