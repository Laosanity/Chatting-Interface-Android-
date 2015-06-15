package com.example.lawrence.chatingroomdemo.messaging_agent;

import com.example.lawrence.chatingroomdemo.message_adapter.ChatMessage;

/**
 * Created by lawrence on 12/6/15.
 */
public class RequestData {

    protected String mRequestText;

    public RequestData(ChatMessage chatMessage) {
        mRequestText = chatMessage.getContent();
    }

    public String getRequestText() {
        return mRequestText;
    }
}
