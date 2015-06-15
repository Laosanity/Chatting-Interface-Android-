package com.example.lawrence.chatingroomdemo.messaging_agent;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lawrence on 12/6/15.
 */
public class ResponseData {

    protected int mResponseCode;
    protected String mResponseText;

    public ResponseData(JSONObject responseJsonData) {

        try {
            mResponseCode = responseJsonData.getInt("code");
            mResponseText = responseJsonData.getString("text");
//        String responseUrl = responseJsonData.getString("url");
//        JSONArray responseListContent = responseJsonData.getJSONArray("list");

            Log.d("Response Code ", String.valueOf(mResponseCode));
            Log.d("Response Text", mResponseText);
//        Log.d("Response Url", responseUrl);
//        Log.d("Response List", mResponseText);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getResponseText() {
        return mResponseText;
    }
}
