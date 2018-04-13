package com.bussinesspoolapp.bussipool;

import android.app.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;

import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import android.widget.TableRow;

import android.widget.TextView;

import android.widget.TableRow.LayoutParams;

import com.beans.JoiningDetailsBean;
import com.beans.UserJoinPaymentBean;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.Util.CommonUtil;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;
import com.ennum.PaymentMode;
import com.ennum.StatusEnum;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.LinkedHashMap;
import java.util.List;

public class Joining extends Activity implements View.OnClickListener,AsyncResponse {

    LinkedHashMap<Integer,UserJoinPaymentBean> joinMap = new LinkedHashMap<>();
//

//
//
    TableLayout tl;
//
    TableRow tr;
//
    TextView companyTV,valueTV;


    String type="";
    ProgressDialog progress ;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_joining);

        tl = (TableLayout) findViewById(R.id.maintable);
//
////        addHeaders();
//
        int schemeId= CommonUtil.getSessionInt(this,"schemeId");
        String schemeName = CommonUtil.getSessionString(this,"schemeName");
        Long userId= CommonUtil.getSessionLong(this,"userId");
//
//
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
            cp.execute("/user/getuserjoinpayment",req);
            type="join";
        } catch (Exception e) {
            e.printStackTrace();
        }





    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void processFinish(String output) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if(type.equals("join")) {
                List<UserJoinPaymentBean> jd = objectMapper.readValue(output, new TypeReference<List<UserJoinPaymentBean>>() {
                });


           int i= 0;
            for(UserJoinPaymentBean up : jd) {/** Create a TableRow dynamically **/
                i++;
                tr = new TableRow(this);
                if (i % 2 == 0) {
                    tr.setBackgroundColor(Color.LTGRAY);

                }
                joinMap.put(i,up);
tr.setId(i);

                tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

                tr.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {

                        final Dialog dialog = new Dialog(Joining.this);
                        dialog.setContentView(R.layout.joiningdetails);
                        UserJoinPaymentBean up = joinMap.get(v.getId());
                        // set the custom dialog components - text, image and button

                        TextView textSr = (TextView) dialog.findViewById(R.id.jdSrNo);
                        textSr.setText(""+v.getId());

                        TextView text = (TextView) dialog.findViewById(R.id.jdDate);
                        text.setText(""+up.getJoindate());

                        TextView textPay = (TextView) dialog.findViewById(R.id.jdPay);
                        textPay.setText("by "+ PaymentMode.getById(up.getPayment_modeid()).getValue());

                        TextView textStatus = (TextView) dialog.findViewById(R.id.jdStatus);
                        textStatus.setText(""+StatusEnum.getById(up.getPaymentstatus()).getValue());

                        TextView textPayReal = (TextView) dialog.findViewById(R.id.jdPayRelease);

                        textPayReal.setText(""+(up.getIsPaymentRealease()==0?"No":"Yes"));

                        TextView textPoolComp = (TextView) dialog.findViewById(R.id.jdPoolComp);
                        textPoolComp.setText(""+(up.getIsPaymentRealease()==0?"No":"Yes"));

                        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                });

                /** Creating a TextView to add to the row **/

                companyTV = new TextView(this);
                companyTV.setText("" + i);
                companyTV.setTextColor(Color.BLACK);
                companyTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                ;
                companyTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                companyTV.setPadding(5, 10, 10, 5);

                tr.addView(companyTV);  // Adding textView to tablerow.
                tr.setPadding(5, 10, 5, 10);
                /** Creating another textview **/

                valueTV = new TextView(this);
                valueTV.setText(""+up.getJoindate());
                valueTV.setTextColor(Color.BLACK);
                valueTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                ;
                valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                valueTV.setPadding(5, 10, 10, 5);
//            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(valueTV); // Adding textView to tablerow.

                valueTV = new TextView(this);
                valueTV.setText("VIEW");

                valueTV.setBackgroundResource(R.color.BussiPoolSecondary);
                valueTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                ;
                valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                valueTV.setPadding(5, 10, 0, 5);
//            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(valueTV); // Adding textView to tablerow.
//
//                valueTV = new TextView(this);
//                valueTV.setText("" + up.getPaymentstatus());
//                valueTV.setTextColor(Color.BLACK);
//                valueTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//                ;
//                valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//                valueTV.setPadding(5, 10, 10, 5);
////            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//                tr.addView(valueTV); // Adding textView to tablerow.
//
//                valueTV = new TextView(this);
//                valueTV.setText("" + up.getIsPaymentRealease());
//                valueTV.setTextColor(Color.BLACK);
//                valueTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//                ;
//                valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//                valueTV.setPadding(5, 10, 10, 5);
////            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//                tr.addView(valueTV); // Adding textView to tablerow.
//
//                valueTV = new TextView(this);
//                valueTV.setText("" + up.getIsExit());
//                valueTV.setTextColor(Color.BLACK);
//                valueTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//                ;
//                valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//                valueTV.setPadding(5, 10, 10, 5);
////            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//                tr.addView(valueTV); // Adding textView to tablerow.
                // Add the TableRow to the TableLayout

                tl.addView(tr, new TableLayout.LayoutParams(

                        LayoutParams.FILL_PARENT,

                        LayoutParams.WRAP_CONTENT));



            }
            }
            progress.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        addData();
    }
}