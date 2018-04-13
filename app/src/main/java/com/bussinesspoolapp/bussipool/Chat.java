package com.bussinesspoolapp.bussipool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.beans.ChatRoomBean;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallGet;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity implements View.OnClickListener, AsyncResponse {
    List<ChatRoomBean> chatList = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;long userId;
    String type="",fname="",lname="";    ProgressDialog progress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("chat");
        SharedPreferences settings = getSharedPreferences("preferences",
                Context.MODE_PRIVATE);

         userId = settings.getLong("userId",0);
        fname=settings.getString("fname","");
        lname=settings.getString("lname","");
        buttonSend = (Button) findViewById(R.id.send);
        listView = (ListView) findViewById(R.id.msgview);
        chatText = findViewById(R.id.msg);
        buttonSend.setOnClickListener(this);
        chatArrayAdapter = new ChatArrayAdapter(this, R.layout.right);
        listView.setAdapter(chatArrayAdapter);
        type="getList";

        CallPost cp = new CallPost(this);
        cp.execute("/Chat/getlist","User");
        progress = new ProgressDialog(this);

        progress.setTitle("Loading");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.show();

    }

    @Override
    public void processFinish(String output) {
        try {
            if(type.equalsIgnoreCase("getList")) {
                chatList = objectMapper.readValue(output, new TypeReference<List<ChatRoomBean>>() {
                });
                for (ChatRoomBean chatRoomBean : chatList) {
                    String msg = "";
                    if (chatRoomBean.getMessage() != null) {
                        msg = chatRoomBean.getMessage();
                    }
                    chatArrayAdapter.add(new ChatMessage(false, msg, chatRoomBean.getName(), chatRoomBean.getTime(), chatRoomBean.getShortForm(), chatRoomBean.getReplyToName(),fname+" "+lname));
                }
                listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                listView.setAdapter(chatArrayAdapter);
                progress.dismiss();
            }
            else{
                Intent myIntentForum = new Intent(Chat.this, Chat.class);

                Chat.this.startActivity(myIntentForum);
                progress.dismiss();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if(chatText.getText().equals(""))
        {
            Toast.makeText(this,"Please Enter Message",Toast.LENGTH_SHORT).show();
        }else{
            ChatRoomBean ch = new ChatRoomBean();
            ch.setUserId((int)userId);
            ch.setMessage(""+chatText.getText());
            try {
                String val = objectMapper.writeValueAsString(ch);
                type="send";
                CallPost cp= new CallPost(this);

                cp.execute("/Chat/sendreply",val);

                progress.setMessage("Sending...");
                progress.setCancelable(false);
                progress.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        chatText.getText();
    }
}
