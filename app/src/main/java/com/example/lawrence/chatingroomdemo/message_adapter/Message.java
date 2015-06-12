package com.example.lawrence.chatingroomdemo.message_adapter;
import com.example.lawrence.chatingroomdemo.messaging_agent.ResponseData;

/**
 * Created by lawrence on 9/6/15.
 */
public class Message {

    public enum MessageType {
        TYPE_RECEIVED,
        TYPE_SENT;
    }

    public enum MessageState {
        STATE_NEW,
        STATE_STORE;
    }

    private String content;
    private MessageType type;
    private MessageState state;

    public Message(String content, MessageType type) {
        this.content = content;
        this.type = type;
        this.state = MessageState.STATE_NEW;
    }

    public MessageState getState() {
        return state;
    }

    public void setState(MessageState state) {
        this.state = state;
    }

    public Message(ResponseData responseData) {
        this.type = MessageType.TYPE_RECEIVED;
        this.content = responseData.getResponseText();
        this.state = MessageState.STATE_NEW;
    }

    public MessageType getType() {
        return type;
    }

    public String getContent() {

        return content;
    }

}
