package com.example.lawrence.chatingroomdemo.messaging_agent;

import com.example.lawrence.chatingroomdemo.message_adapter.ChatMessage;

/**
 * Created by lawrence on 10/6/15.
 */
public interface TuringResponseInterface {

    void returnResponseData(ChatMessage responseChatMessage);
}
