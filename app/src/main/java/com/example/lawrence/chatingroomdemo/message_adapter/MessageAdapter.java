package com.example.lawrence.chatingroomdemo.message_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lawrence.chatingroomdemo.R;

import java.util.List;

/**
 * Created by lawrence on 9/6/15.
 */
public class MessageAdapter extends ArrayAdapter<Message> {

    private int resourceId;

    Context currentContext;

    public MessageAdapter(Context context, int textViewResourceId, List<Message> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        currentContext = context;
    }

    /* reuse view */
    /* convertView only store the view structure without view's state */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message message = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();

            viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_message_layout);
            viewHolder.leftMessage = (TextView) view.findViewById(R.id.left_message_textview);
            viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_message_layout);
            viewHolder.rightMessage = (TextView) view.findViewById(R.id.right_message_textview);

            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (message.getType() == Message.MessageType.TYPE_RECEIVED) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMessage.setText(message.getContent());

        } else if (message.getType() == Message.MessageType.TYPE_SENT) {
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.rightMessage.setText(message.getContent());
        }

        if (message.getState() == Message.MessageState.STATE_NEW) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_in);
            view.startAnimation(animation);
            message.setState(Message.MessageState.STATE_STORE);
        }

        return view;
    }

    // if set as static class then it will be shared among all instance of this class
    class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMessage;
        TextView rightMessage;
    }
}
