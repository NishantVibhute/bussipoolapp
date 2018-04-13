package com.bussinesspoolapp.bussipool;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.beans.PassRowBean;
import com.beans.SchemeJoinBean;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.Util.CommonUtil;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;
import com.ennum.PaymentMode;
import com.ennum.StatusEnum;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.LinkedHashMap;
import java.util.List;

public class Account extends AppCompatActivity implements AsyncResponse {
    ProgressDialog progress ;
    TableLayout tl;
    LinkedHashMap<Integer,PassRowBean> joinMap = new LinkedHashMap<>();
    //
    TableRow tr;
    TextView rowCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        tl = (TableLayout) findViewById(R.id.accounttable);
//
////        addHeaders();
//
        int schemeId= CommonUtil.getSessionInt(this,"schemeId");
        String schemeName = CommonUtil.getSessionString(this,"schemeName");
        long userId= CommonUtil.getSessionLong(this,"userId");
//
//
        progress = new ProgressDialog(this);

        progress.setTitle("Loading");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.show();
        SchemeJoinBean sjb = new SchemeJoinBean();
        sjb.setSchemeId(schemeId);
        sjb.setUserId((int) userId);

        String req = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            req = objectMapper.writeValueAsString(sjb);
            CallPost cp = new CallPost(this);
            cp.execute("/account/getUserSchemePassbook",req);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(String output) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            List<PassRowBean> passRowBean = objectMapper.readValue(output, new TypeReference<List<PassRowBean>>() {
            });

            int i= 0;
            if(passRowBean.size()!=0) {
                int textS = 13;
                for (PassRowBean up : passRowBean) {/** Create a TableRow dynamically **/
                    i++;
                    tr = new TableRow(this);
                    if (i % 2 == 0) {
                        tr.setBackgroundColor(Color.LTGRAY);

                    }
                    joinMap.put(i,up);
                    tr.setId(i);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                    tr.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {

                            final Dialog dialog = new Dialog(Account.this);
                            dialog.setContentView(R.layout.accountdialog);
                            PassRowBean up = joinMap.get(v.getId());
                            // set the custom dialog components - text, image and button

                            TextView textSr = (TextView) dialog.findViewById(R.id.pdSrNo);
                            textSr.setText(""+v.getId());

                            TextView text = (TextView) dialog.findViewById(R.id.pdDate);
                            text.setText(""+up.getDate());

                            TextView textPay = (TextView) dialog.findViewById(R.id.pdPart);
                            textPay.setText(""+ up.getParticulars());

                            TextView textStatus = (TextView) dialog.findViewById(R.id.pdWith);
                            textStatus.setText(""+ up.getWithdrawl());

                            TextView textPayReal = (TextView) dialog.findViewById(R.id.pdDepo);

                            textPayReal.setText(""+up.getDeposit());

                            TextView textPoolComp = (TextView) dialog.findViewById(R.id.pdBal);
                            textPoolComp.setText(""+up.getBalance());

                            Button dialogButton = (Button) dialog.findViewById(R.id.pddialogButtonOK);
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

                    rowCol = new TextView(this);
                    rowCol.setText("" + i);
                   rowCol.setTextSize(textS);
//                    rowCol.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    rowCol.setTextColor(Color.BLACK);
                    rowCol.setPadding(5, 10, 0, 10);
                    tr.addView(rowCol); // Adding textView to tablerow.

                    rowCol = new TextView(this);
                    rowCol.setText("" + up.getDate());
                    rowCol.setTextSize(textS);
                    rowCol.setTextColor(Color.BLACK);
                    rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    rowCol.setPadding(5, 10, 0, 10);
                    tr.addView(rowCol); // Adding textView to tablerow.


                    rowCol = new TextView(this);
                    rowCol.setText("" + up.getWithdrawl());
                    rowCol.setTextSize(textS);
                    rowCol.setTextColor(Color.BLACK);
                    rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    rowCol.setPadding(5, 10, 0, 10);
                    tr.addView(rowCol); // Adding textView to tablerow.

                    rowCol = new TextView(this);
                    rowCol.setText("" + up.getDeposit()); rowCol.setTextSize(textS);
                    rowCol.setTextColor(Color.BLACK);
                    rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    rowCol.setPadding(5, 10, 0, 10);
                    tr.addView(rowCol); // Adding textView to tablerow.

                    rowCol = new TextView(this);
                    rowCol.setText("" + up.getBalance());
                    rowCol.setTextSize(textS);
                    rowCol.setTextColor(Color.BLACK);
                    rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    rowCol.setPadding(5, 10, 0, 10);
                    tr.addView(rowCol); // Adding textView to tablerow.

                    rowCol = new TextView(this);
                    rowCol.setText("VIEW" );
                    rowCol.setTextSize(textS);
                    rowCol.setTextColor(Color.BLUE);
                    rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    rowCol.setPadding(5, 10, 0, 10);
                    tr.addView(rowCol); // Adding textView to tablerow.

                    tl.addView(tr, new TableLayout.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
            else{
                rowCol = new TextView(this);
                rowCol.setText("No Data Available");
                rowCol.setTextColor(Color.BLACK);
                rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                rowCol.setPadding(5, 10, 5, 10);
                tr.addView(rowCol); // Adding textView to tablerow.

                TableRow.LayoutParams params = (TableRow.LayoutParams)rowCol.getLayoutParams();
                params.span = 6;
                rowCol.setLayoutParams(params);
            }
            progress.dismiss();
        }catch (Exception e)
        {
            progress.dismiss();
            e.printStackTrace();
        }

    }
}
