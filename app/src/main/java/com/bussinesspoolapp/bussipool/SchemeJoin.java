package com.bussinesspoolapp.bussipool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beans.SchemeBean;
import com.beans.SchemeJoinBean;
import com.beans.UserBean;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class SchemeJoin extends AppCompatActivity implements View.OnClickListener,AsyncResponse {

    Button butJoin;
    RadioButton radCash,radCheq,radNet;
    TextView txtPayDetails,schemeName,schemeDesc,schemeStartDate,schemeAmount,schemeMemberPerc;
    int payId;
    long userId;
    int schemeId;
    ObjectMapper objectMapper
            = new ObjectMapper();
    String type="";
    RadioGroup radioGroup;

    private static final String PREFS_NAME = "preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        userId= settings.getLong("userId",0);
        Intent intent = getIntent();
        schemeId = intent.getExtras().getInt("schemeId");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_join);
        butJoin = findViewById(R.id.btnJoin);
        radCash = findViewById(R.id.radCash);
        radCheq = findViewById(R.id.radCheq);
        radNet = findViewById(R.id.radNetBanking);
        txtPayDetails = findViewById(R.id.txtPayDetails);
        schemeName= findViewById(R.id.schemeName);
                schemeDesc= findViewById(R.id.SchemeDescription);
//                schemeStartDate= findViewById(R.id.schemeStartDate);
                schemeAmount= findViewById(R.id.schemeAmount);
//                schemeMemberPerc= findViewById(R.id.schemeMemberPerc);
        schemeDesc.setMovementMethod(new ScrollingMovementMethod());
        CallPost callPost=new CallPost(this);
        type="schemeDetails";
        callPost.execute("/scheme/getschemedetail",""+schemeId);


        radioGroup = (RadioGroup) findViewById(R.id.radgrpPay);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                txtPayDetails.setTextColor(Color.BLACK);
                // checkedId is the RadioButton selected
                if(checkedId==R.id.radCash)
                {
                    txtPayDetails.setText("Pay by Cash");
                    payId=1;
                }else if(checkedId==R.id.radCheq)
                {
                    txtPayDetails.setText("Pay by Cheque");
                    payId=2;
                }
                else if(checkedId==R.id.radNetBanking)
                {
                    txtPayDetails.setText("Pay by NetBanking");
                    payId=3;
                }
            }
        });

        butJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(payId ==0)
        {
            txtPayDetails.setTextColor(Color.RED);
            txtPayDetails.setText("Please Select Payment Mode");
        }else {


            if (userId == 0) {
                Intent i = new Intent(SchemeJoin.this, CreateUser.class);
                i.putExtra("schemeId", schemeId);
                i.putExtra("payId", payId);

                startActivity(i);
//
            } else {
                SchemeJoinBean sj = new SchemeJoinBean();
                sj.setUserId((int) userId);
                sj.setSchemeId(schemeId);
                sj.setPaymentModeId(payId);
                sj.setUserStatus(1);
                sj.setMemberType(1);
                try {
                    String a = objectMapper.writeValueAsString(sj);
                    type = "schemeJoin";
                    CallPost cp = new CallPost(this);
                    cp.execute("/scheme/join", a);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void processFinish(String output) {
        try {
            if(type.equals("schemeDetails")) {
                SchemeBean up = objectMapper.readValue(output, SchemeBean.class);
                schemeName.setText(up.getSchemeName());
                schemeDesc.setText(up.getSchemeDescription());
//                schemeStartDate.setText(up.getStartDate());
                schemeAmount.setText(up.getAmount() + " /-");
//                schemeMemberPerc.setText(up.getMemberPerc() + " %");
            }else{
                Intent i = new Intent(SchemeJoin.this,Home.class);
                startActivity(i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

