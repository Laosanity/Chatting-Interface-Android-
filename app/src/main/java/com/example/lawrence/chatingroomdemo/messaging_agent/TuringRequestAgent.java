package com.example.lawrence.chatingroomdemo.messaging_agent;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lawrence.chatingroomdemo.message_adapter.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*
100000	文本类数据
305000	列车
306000	航班
200000	网址类数据
302000	新闻
308000	菜谱、视频、小说
40001	key的长度错误（32位）
40002	请求内容为空
40003	key错误或帐号未激活
40004	当天请求次数已用完
40005	暂不支持该功能
40006	服务器升级中
40007	服务器数据格式异常
* */

/*
*  TODO: Remember to check http connection state before you post any request
*  TODO: More error handles for http connection.
*
* */

/*
*  Remember: do not use singleton as a AsyncTask, it only executes once
* */

/**
 * Created by lawrence on 10/6/15.
 */

class ResponseCode {
    public static final int RESPONSE_OK = 200;
}

public class TuringRequestAgent {

    // for testing purpose
    private final static String REQUEST_URL = "http://www.tuling123.com/openapi/api?key=_KEY&userid=_USER_ID&info=_INFO";
    private final static String USER_ID = "97175";
    private final static String API_KEY = "a431488cc222ef5c05d0e5203d632452";

    private static TuringRequestAgent sOurInstance = null;
    private TuringResponseInterface mResponseInterface = null;
    private String mBaseUrlString;

    private TuringRequestAgent() {
        mBaseUrlString = REQUEST_URL.replaceAll("_KEY", API_KEY);
        mBaseUrlString = mBaseUrlString.replaceAll("_USER_ID", USER_ID);
    }

    public static TuringRequestAgent getInstance() {
        // lazy load
        if (sOurInstance == null) {
            // thread safe
            // Actually we can initiate it once this static class is loaded without lazy loading.
            synchronized (TuringRequestAgent.class) {
                sOurInstance = new TuringRequestAgent();
            }
        }
        return sOurInstance;
    }

    public void registerResponseHandler(TuringResponseInterface implementedInterface) {
        mResponseInterface = implementedInterface;
    }

    public void postMessage(ChatMessage chatMessage) {

        RequestData requestData = new RequestData(chatMessage);
        // create a async task
        PostMessageTask newTask = new PostMessageTask(mBaseUrlString, mResponseInterface);
        newTask.execute(requestData);
    }
}

class PostMessageTask extends AsyncTask<RequestData, Void, ResponseData> {

    private String mBaseUrlString;
    private TuringResponseInterface mResponseInterface = null;

    public PostMessageTask(String baseUrl, TuringResponseInterface implementedInterface) {
        mBaseUrlString = baseUrl;
        mResponseInterface = implementedInterface;
    }

    // on excuting thread
    @Override
    protected ResponseData doInBackground(RequestData... params) {

        try {
            RequestData requestData = params[0];
            String messageContent = requestData.getRequestText();
            String encodedPara = URLEncoder.encode(messageContent, "UTF-8");
            String requestUrlString = mBaseUrlString.replaceAll("_INFO", encodedPara);
            Log.d("Request URL ", requestUrlString);
            return postRequest(requestUrlString);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ResponseData result) {
        // back to main thread, callback
        if (mResponseInterface != null && result != null) {

            ChatMessage responseChatMessage = new ChatMessage(result);
            mResponseInterface.returnResponseData(responseChatMessage);
        }

    }

    /* util methods */
    // return an InputStream is dangerous coz it's stateful
    private ResponseData postRequest(String urlString) throws IOException {

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5 sec
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == ResponseCode.RESPONSE_OK) {
                inputStream = connection.getInputStream();
                return convertInputStreamToResponseData(inputStream);
            }
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    // utils, could be override by other types of data request
    protected ResponseData convertInputStreamToResponseData(InputStream inputStream) {
        try {
            // decorator pattern
            BufferedReader steamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder responseStringBuilder = new StringBuilder(); // different with StringBuffer (sync)

            String inputString;
            while ((inputString = steamReader.readLine()) != null) {
                responseStringBuilder.append(inputString);
            }
            JSONObject responseJsonData = new JSONObject(responseStringBuilder.toString());
            return new ResponseData(responseJsonData);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
