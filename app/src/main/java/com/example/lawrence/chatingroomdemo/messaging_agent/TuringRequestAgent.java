package com.example.lawrence.chatingroomdemo.messaging_agent;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lawrence.chatingroomdemo.message_adapter.Message;

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

    private static TuringRequestAgent ourInstance = null;
    private static TuringResponseInterface responseInterface = null;

    private String baseUrlString;

    public static TuringRequestAgent getInstance() {

        // lazy load
        if (ourInstance == null) {
            // thread safe
            // Actually we can initiate it once this static class is loaded without lazy loading.
            synchronized (TuringRequestAgent.class) {
                ourInstance = new TuringRequestAgent();
            }
        }
        return ourInstance;
    }

    private TuringRequestAgent() {

        baseUrlString = REQUEST_URL.replaceAll("_KEY", API_KEY);
        baseUrlString = baseUrlString.replaceAll("_USER_ID", USER_ID);
    }

    public void registerResponseHandler(TuringResponseInterface implementedInterface) {
        responseInterface = implementedInterface;
    }

    public void postMessage(Message message) {

        RequestData requestData = new RequestData(message);
        // create a async task
        PostMessageTask newTask = new PostMessageTask(baseUrlString, responseInterface);
        newTask.execute(requestData);
    }
}

class PostMessageTask extends AsyncTask<RequestData, Void, ResponseData> {

    String baseUrlString;
    TuringResponseInterface responseInterface = null;

    public PostMessageTask(String baseUrl, TuringResponseInterface implementedInterface) {
        baseUrlString = baseUrl;
        responseInterface = implementedInterface;
    }

    // on excuting thread
    @Override
    protected ResponseData doInBackground(RequestData... params) {

        try {
            RequestData requestData = params[0];
            String messageContent = requestData.getRequestText();
            String encodedPara = URLEncoder.encode(messageContent, "UTF-8");
            String requestUrlString = baseUrlString.replaceAll("_INFO", encodedPara);
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
        if (responseInterface != null && result != null) {

            Message responseMessage = new Message(result);
            responseInterface.returnResponseData(responseMessage);
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
