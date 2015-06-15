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
public class MessageAdapter extends ArrayAdapter<ChatMessage> {

    private int mResourceId;

    public MessageAdapter(Context context, int textViewResourceId, List<ChatMessage> objects) {
        super(context, textViewResourceId, objects);
        mResourceId = textViewResourceId;
    }

    /* reuse view */
    /* convertView only store the view structure without view's state */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMessage chatMessage = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(mResourceId, null);

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

        if (chatMessage.getType() == ChatMessage.Type.TYPE_RECEIVED) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMessage.setText(chatMessage.getContent());

        } else if (chatMessage.getType() == ChatMessage.Type.TYPE_SENT) {
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.rightMessage.setText(chatMessage.getContent());
        }

        if (chatMessage.getState() == ChatMessage.State.STATE_NEW) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_in);
            view.startAnimation(animation);
            chatMessage.setState(ChatMessage.State.STATE_STORE);
        }

        return view;
    }

    static class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMessage;
        TextView rightMessage;
    }
}