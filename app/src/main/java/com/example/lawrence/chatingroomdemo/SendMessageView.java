package com.example.lawrence.chatingroomdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.lawrence.chatingroomdemo.message_adapter.MessageReceiverInterface;

/**
 * Created by lawrence on 9/6/15.
 */

public class SendMessageView extends LinearLayout {

    MessageReceiverInterface messageInterface;
    EditText editText;
    Button sendButton;

    public void setMessageInterface(MessageReceiverInterface implementClass){
        this.messageInterface = implementClass;
    }

    public SendMessageView(Context context, AttributeSet attrs){

        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.chatting_input_view, this);

        editText = (EditText) findViewById(R.id.chatting_input_textview);
        sendButton = (Button) findViewById(R.id.chatting_send_button);

        sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // send out inputted message
                // may use try-catch later
                if(messageInterface != null) {
                    messageInterface.sendMessage(editText.getText().toString());
                }
            }
        });

    }

    // clear content in input textfield
    public void clear(){
        editText.setText("");
    }
}
