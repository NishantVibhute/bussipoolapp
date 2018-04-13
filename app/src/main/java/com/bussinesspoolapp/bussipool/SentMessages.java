package com.bussinesspoolapp.bussipool;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.beans.JoiningDetailsBean;
import com.beans.SchemeBean;
import com.beans.SentMessageBean;
import com.beans.UserJoinPaymentBean;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.Util.CommonUtil;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.List;

public class SentMessages extends AppCompatActivity implements AsyncResponse{
    TableLayout tl;
    //
    TableRow tr;
    //
    TextView companyTV,valueTV;


    String type="";
    ProgressDialog progress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_messages);

        tl = (TableLayout) findViewById(R.id.maintableSMS);
//
////        addHeaders();
//
        int schemeId= CommonUtil.getSessionInt(this,"schemeId");
        String schemeName = CommonUtil.getSessionString(this,"schemeName");
        Long userId= CommonUtil.getSessionLong(this,"userId");
        progress = new ProgressDialog(this);

        progress.setTitle("Loading");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.show();
        JoiningDetailsBean jdb = new JoiningDetailsBean();
        jdb.setUserId(userId);
        jdb.setSchemeId(schemeId);
        String req = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            req = objectMapper.writeValueAsString(jdb);
            CallPost cp = new CallPost(this);
            cp.execute("/message/getUserSentListWithCount",req);
            type="join";
        } catch (Exception e) {
            e.printStackTrace();
        }

   }

    @Override
    public void processFinish(String output) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if(type.equals("join")) {
            List<SentMessageBean> sentMessageList = objectMapper.readValue(output, new TypeReference<List<SentMessageBean>>() {
            });

            if(sentMessageList.size()!=0) {

                int i = 0;
                for (SentMessageBean up : sentMessageList) {/** Create a TableRow dynamically **/
                    i++;
                    tr = new TableRow(this);
                    if (i % 2 == 0) {
                        tr.setBackgroundColor(Color.LTGRAY);

                    }


                    companyTV = new TextView(this);
                    companyTV.setText("" + i);
                    companyTV.setTextColor(Color.BLACK);
                    companyTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    ;
                    companyTV.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    companyTV.setPadding(5, 10, 10, 5);

                    tr.addView(companyTV);  // Adding textView to tablerow.
                    tr.setPadding(5, 10, 5, 10);
                    /** Creating another textview **/

                    valueTV = new TextView(this);
                    valueTV.setText("" + up.getTo());
                    valueTV.setTextColor(Color.BLACK);
                    valueTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    ;
                    valueTV.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    valueTV.setPadding(5, 10, 10, 5);
//            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    tr.addView(valueTV); // Adding textView to tablerow.

                    valueTV = new TextView(this);
                    valueTV.setText("" + up.getCount());
                    valueTV.setTextColor(Color.BLACK);
                    valueTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    ;
                    valueTV.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    valueTV.setPadding(5, 10, 10, 5);
//            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    tr.addView(valueTV); // Adding textView to tablerow.

                    tl.addView(tr, new TableLayout.LayoutParams(

                            TableRow.LayoutParams.FILL_PARENT,

                            TableRow.LayoutParams.WRAP_CONTENT));


                }
            }else{
                tr = new TableRow(this);
                valueTV = new TextView(this);
                valueTV.setText("No data Available");

                valueTV.setBackgroundResource(R.color.BussiPoolSecondary);
                valueTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                ;
                valueTV.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                valueTV.setPadding(5, 10, 0, 5);
                TableRow.LayoutParams params = (TableRow.LayoutParams)valueTV.getLayoutParams();
                params.span = 6;
                valueTV.setLayoutParams(params);
//            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(valueTV); // Adding textView to tablerow.
//
                tl.addView(tr, new TableLayout.LayoutParams(

                        TableRow.LayoutParams.FILL_PARENT,

                        TableRow.LayoutParams.WRAP_CONTENT));
            }
            }


            } catch(Exception e){
                e.printStackTrace();
            }
        progress.dismiss();
        }
    }

