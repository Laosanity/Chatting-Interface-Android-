package com.example.lawrence.chatingroomdemo.message_adapter;

import com.example.lawrence.chatingroomdemo.messaging_agent.ResponseData;

/**
 * Created by lawrence on 9/6/15.
 */
public class ChatMessage {

    private String mContent;
    private Type mType;
    private State mState;

    public enum Type { TYPE_RECEIVED, TYPE_SENT; }
    public enum State { STATE_NEW, STATE_STORE; }

    public ChatMessage(String content, Type type) {
        mContent = content;
        mType = type;
        mState = State.STATE_NEW;
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        this.mState = state;
    }

    public ChatMessage(ResponseData responseData) {
        this.mType = Type.TYPE_RECEIVED;
        this.mContent = responseData.getResponseText();
        this.mState = State.STATE_NEW;
    }

    public Type getType() {
        return mType;
    }

    public String getContent() {
        return mContent;
    }

}
