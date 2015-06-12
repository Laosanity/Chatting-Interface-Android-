package com.example.lawrence.chatingroomdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lawrence.chatingroomdemo.message_adapter.Message;
import com.example.lawrence.chatingroomdemo.message_adapter.MessageAdapter;
import com.example.lawrence.chatingroomdemo.message_adapter.MessageReceiverInterface;
import com.example.lawrence.chatingroomdemo.messaging_agent.TuringRequestAgent;
import com.example.lawrence.chatingroomdemo.messaging_agent.TuringResponseInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
        implements MessageReceiverInterface, TuringResponseInterface {

    private ListView chattingMessageListView;

    private MessageAdapter messageListViewAdapter;

    private SendMessageView sendMessageView;

    private List<Message> dataList;

    private boolean toogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataSource();
        initWidgetForActivity();
    }

    private void initDataSource() {
        dataList = new ArrayList<>();
        TuringRequestAgent.getInstance().registerResponseHandler(MainActivity.this);
    }

    private void initWidgetForActivity() {

        chattingMessageListView = (ListView) findViewById(R.id.chatting_message_listview);
        messageListViewAdapter = new MessageAdapter(MainActivity.this, R.layout.message_items_layout, dataList);
        chattingMessageListView.setAdapter(messageListViewAdapter);

        sendMessageView = (SendMessageView) findViewById(R.id.chatting_sendmessage_view);
        findViewById(R.id.chatting_sendmessage_view);
        sendMessageView.setMessageInterface(this);
    }

    /*  Implement methods for Interface */
    // receive sent message content
    @Override
    public void sendMessage(String content) {
        // more content checking for valid content format
        if (content.length() > 0) {
            Message newMessage = new Message(content, Message.MessageType.TYPE_SENT);
            messageListViewAdapter.add(newMessage);
            // jump to lateset message textview
            chattingMessageListView.setSelection(dataList.size());
            // send message
            TuringRequestAgent.getInstance().postMessage(newMessage);
            // clear input textfield
            sendMessageView.clear();
        }

    }

    // receive sent message content
    // TODO: combine with void sendMessage()
    @Override
    public void returnResponseData(Message responseMessage) {

        messageListViewAdapter.add(responseMessage);
        // jump to lateset message textview
        chattingMessageListView.setSelection(dataList.size());
    }
}
