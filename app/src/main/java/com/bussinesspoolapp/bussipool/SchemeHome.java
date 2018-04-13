package com.bussinesspoolapp.bussipool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.beans.JoiningDetailsBean;
import com.beans.PassRowBean;
import com.beans.SchemeBean;
import com.beans.SchemeJoinBean;
import com.bussinesspoolapp.bussipool.Adapter.CustomGrid;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.Util.CommonUtil;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SchemeHome extends AppCompatActivity implements View.OnClickListener,AsyncResponse {

    private static final String PREFS_NAME = "preferences";
    ObjectMapper objectMapper = new ObjectMapper();
    long userId;
            int schemeId;
    String schemeName,type="";
    TextView txtMsg,txtMsg1,txtMsg2 ;
    List<JoiningDetailsBean> joiningDetailsBeans = new ArrayList<>();
    String userName;ProgressDialog progress ;

    GridView grid;
    String[] web = {
            "PROMOTE",
            "ACCOUNT",
            "JOINING",
            "PAYMENT",
            "FORUM",
            "INFO",
            "SENT SMS"

    } ;
    int[] imageId = {
            R.mipmap.promote,
            R.mipmap.account,
            R.mipmap.joining,
            R.mipmap.payment,
            R.mipmap.forum,
            R.mipmap.info,
            R.mipmap.info
    };
    String[] gridColor ={

            "#FFF5F5F5",
            "#FFFFFFFF",
            "#FFFFFFFF",
            "#FFF5F5F5",
            "#FFF5F5F5",
            "#FFFFFFFF",
            "#FFF5F5F5"

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_home);

        if(CommonUtil.isNetworkAvailable(this)) {


                schemeId = CommonUtil.getSessionInt(this, "schemeId");
                schemeName = CommonUtil.getSessionString(this, "schemeName");
                userId = CommonUtil.getSessionLong(this, "userId");
                userName = CommonUtil.getSessionString(this, "Username");

                CustomGrid adapter = new CustomGrid(SchemeHome.this, web, imageId, gridColor);
                grid = (GridView) findViewById(R.id.grid);
                grid.setAdapter(adapter);


                this.setTitle(schemeName);
                txtMsg = findViewById(R.id.txtMsg);
                txtMsg1 = findViewById(R.id.txtMsg1);
                txtMsg2 = findViewById(R.id.txtMsg2);
                progress = new ProgressDialog(this);

                progress.setTitle("Loading");
                progress.setMessage("Please Wait");
                progress.setCancelable(false);
                progress.show();

                JoiningDetailsBean bean = new JoiningDetailsBean();
                bean.setUserId(userId);
                bean.setSchemeId(schemeId);


                CallPost cp = new CallPost(this);

                try {
                    String val = objectMapper.writeValueAsString(bean);
                    cp.execute("/user/userschemejoininglist", val);
                    type = "schemeList";


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this,"No internet connection",Toast.LENGTH_LONG).show();
            }


    }




    @Override
    public void onClick(View v) {


    }

    @Override
    public void processFinish(String output) {

        if(type.equals("schemeList"))
        {
            try {
                joiningDetailsBeans = objectMapper.readValue(output,new TypeReference<List<JoiningDetailsBean>>(){});
                int size = joiningDetailsBeans.size();
                if(size==0)
                {
                    Intent i = new Intent(SchemeHome.this,SchemeJoin.class);
                    i.putExtra("schemeId", schemeId);
                    i.putExtra("schemeName",schemeName);
                    startActivity(i);
                }
                else{
                    JoiningDetailsBean jd = joiningDetailsBeans.get(size-1);
                    String msg="";
                    txtMsg.setText("Welcome, "+userName);
                    if(jd.getUser_status()==1)
                    {
                        txtMsg1.setText("Your request is waiting for approval from admin");
                        if(jd.getPaymodeId()==1)
                        {
                            msg = msg+"Pay your ammount by cash\n";
                        }else if(jd.getPaymodeId()==2)
                        {
                            msg = msg+"Pay your ammount by cheque. Named as 'Bussiness Pool limited'\n";
                        }else if(jd.getPaymodeId()==1)
                        {
                            msg = msg+"Pay your ammount by Netbanking to Bank Name: HDFC, Account No:458745\n";
                        }
                        msg=msg+"\n If already paid then Please wait for approval";
                        txtMsg2.setText("");
                        progress.dismiss();
                        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                Toast.makeText(SchemeHome.this,"Sorry Your request is not yet approved",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                    SchemeJoinBean sjb = new SchemeJoinBean();
                    sjb.setSchemeId(schemeId);
                    sjb.setUserId((int) userId);
                    String val = objectMapper.writeValueAsString(sjb);
                    CallPost cp1 = new CallPost(this);
                    cp1.execute("/account/getUserSchemePassbook",val);
                    type="balance";

                        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {

                                switch (web[+position]) {
                                    case "PROMOTE":
                                        Intent myIntent = new Intent(SchemeHome.this, Promote.class);
                                        myIntent.putExtra("userId", userId);
                                        myIntent.putExtra("schemeId", schemeId);
                                        SchemeHome.this.startActivity(myIntent);

                                        break;
                                    case "FORUM":
                                        Intent myIntentForum = new Intent(SchemeHome.this, Chat.class);
                                        myIntentForum.putExtra("userId", userId);
                                        myIntentForum.putExtra("schemeId", schemeId);
                                        SchemeHome.this.startActivity(myIntentForum);

                                        break;
                                    case "JOINING":
                                        Intent myIntentJOINING = new Intent(SchemeHome.this, Joining.class);
                                        myIntentJOINING.putExtra("userId", userId);
                                        myIntentJOINING.putExtra("schemeId", schemeId);
                                        SchemeHome.this.startActivity(myIntentJOINING);

                                        break;
                                    case "ACCOUNT":
                                        Intent myIntentACCOUNT = new Intent(SchemeHome.this, Account.class);
                                        myIntentACCOUNT.putExtra("userId", userId);
                                        myIntentACCOUNT.putExtra("schemeId", schemeId);
                                        SchemeHome.this.startActivity(myIntentACCOUNT);

                                        break;
                                    case "INFO":
                                        Intent myIntentINFO = new Intent(SchemeHome.this, Video.class);
                                        myIntentINFO.putExtra("userId", userId);
                                        myIntentINFO.putExtra("schemeId", schemeId);
                                        SchemeHome.this.startActivity(myIntentINFO);

                                        break;
                                    case "PAYMENT":
                                        Intent myIntentPAYMENT = new Intent(SchemeHome.this, Payment.class);
                                        myIntentPAYMENT.putExtra("userId", userId);
                                        myIntentPAYMENT.putExtra("schemeId", schemeId);
                                        SchemeHome.this.startActivity(myIntentPAYMENT);

                                        break;
                                    case "SENT SMS":
                                        Intent myIntentSent = new Intent(SchemeHome.this, SentMessages.class);
                                        myIntentSent.putExtra("userId", userId);
                                        myIntentSent.putExtra("schemeId", schemeId);
                                        SchemeHome.this.startActivity(myIntentSent);

                                        break;
                                }

                            }
                        });
                    }

//                    else if(jd.getUser_status()==2)
//                    {
//
//                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<PassRowBean> passRowBean = objectMapper.readValue(output, new TypeReference<List<PassRowBean>>() {
                });

                int siz = passRowBean.size();
                if(siz!=0) {
                    PassRowBean p = passRowBean.get(siz - 1);
                    txtMsg1.setText("Balance. "+p.getDate());
                    txtMsg2.setText("INR "+p.getBalance()+"/- ");
                }
                else{

                    txtMsg1.setText("");
                    txtMsg2.setText("");
                }
                progress.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
