package com.example.lawrence.chatingroomdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lawrence.chatingroomdemo.message_adapter.ChatMessage;
import com.example.lawrence.chatingroomdemo.message_adapter.MessageAdapter;
import com.example.lawrence.chatingroomdemo.message_adapter.MessageReceiverInterface;
import com.example.lawrence.chatingroomdemo.messaging_agent.TuringRequestAgent;
import com.example.lawrence.chatingroomdemo.messaging_agent.TuringResponseInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
        implements MessageReceiverInterface, TuringResponseInterface {

    private ListView mChattingMessageListView;
    private MessageAdapter mMessageListViewAdapter;
    private SendMessageView mSendMessageView;
    private List<ChatMessage> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataSource();
        initWidgetForActivity();
    }

    private void initDataSource() {
        mDataList = new ArrayList<>();
        TuringRequestAgent.getInstance().registerResponseHandler(MainActivity.this);
    }

    private void initWidgetForActivity() {

        mChattingMessageListView = (ListView) findViewById(R.id.chatting_message_listview);
        mMessageListViewAdapter = new MessageAdapter(MainActivity.this, R.layout.message_items_layout, mDataList);
        mChattingMessageListView.setAdapter(mMessageListViewAdapter);

        mSendMessageView = (SendMessageView) findViewById(R.id.chatting_sendmessage_view);
        findViewById(R.id.chatting_sendmessage_view);
        mSendMessageView.setMessageInterface(this);
    }

    /*  Implement methods for Interface */
    // receive sent message content
    @Override
    public void sendMessage(String content) {
        // more content checking for valid content format
        if (content.length() > 0) {
            ChatMessage newChatMessage = new ChatMessage(content, ChatMessage.Type.TYPE_SENT);
            mMessageListViewAdapter.add(newChatMessage);
            // jump to lateset message textview
            mChattingMessageListView.setSelection(mDataList.size());
            // send message
            TuringRequestAgent.getInstance().postMessage(newChatMessage);
            // clear input textfield
            mSendMessageView.clear();
        }

    }

    // receive sent message content
    // TODO: combine with void sendMessage()
    @Override
    public void returnResponseData(ChatMessage responseChatMessage) {

        mMessageListViewAdapter.add(responseChatMessage);
        // jump to lateset message textview
        mChattingMessageListView.setSelection(mDataList.size());
    }
}
