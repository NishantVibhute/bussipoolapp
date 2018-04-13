package com.bussinesspoolapp.bussipool;

/**
 * Created by Nishant on 2/27/2018.
 */


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;

    private TextView chatName;
    private TextView chatTime;
    private TextView chatLogo;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private Context context;

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.left) {
            row = inflater.inflate(R.layout.right, parent, false);
        }else{
            row = inflater.inflate(R.layout.left, parent, false);
        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        chatName = (TextView) row.findViewById(R.id.msgTitle);
        if(chatMessageObj.replyTo == null)
        {
            chatText.setText(chatMessageObj.message);
        }else{
            chatText.setText("@"+chatMessageObj.replyTo+" "+chatMessageObj.message);
            if(chatMessageObj.replyTo.equalsIgnoreCase(chatMessageObj.userName))
            {
                chatName.setTextColor(Color.parseColor("#006400"));
            }
        }



        chatName.setText(chatMessageObj.name);



        chatTime = (TextView) row.findViewById(R.id.txtTime);
        chatTime.setText(chatMessageObj.dateTime);
        chatLogo = (TextView) row.findViewById(R.id.txtLogo);
        chatLogo.setText(chatMessageObj.shortForm);

        return row;
    }
}