package com.example.lawrence.chatingroomdemo.messaging_agent;

import com.example.lawrence.chatingroomdemo.message_adapter.Message;

/**
 * Created by lawrence on 12/6/15.
 */
public class RequestData {
    protected String requestText;

    public RequestData(Message message) {
        requestText = message.getContent();
    }

    public String getRequestText() {
        return requestText;
    }
}
