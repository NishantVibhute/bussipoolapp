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

import com.beans.JoiningDetailsBean;
import com.beans.PassRowBean;
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

public class Payment extends AppCompatActivity implements AsyncResponse{

    ProgressDialog progress ;
    TableLayout tl;
    LinkedHashMap<Integer,PassRowBean> joinMap = new LinkedHashMap<>();
    //
    TableRow tr;
    TextView rowCol;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

                tl = (TableLayout) findViewById(R.id.paymenttable);
//
////        addHeaders();
//
        int schemeId= CommonUtil.getSessionInt(this,"schemeId");
        String schemeName = CommonUtil.getSessionString(this,"schemeName");
        long userId= CommonUtil.getSessionLong(this,"userId");

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
            cp.execute("/user/getPaymentDetails",req);
            type="join";
        } catch (Exception e) {
            e.printStackTrace();
        }
//
    }

    @Override
    public void processFinish(String output) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if(type.equals("join")) {
                List<UserJoinPaymentBean> jd = objectMapper.readValue(output, new TypeReference<List<UserJoinPaymentBean>>() {
                });


                int i= 0;
                if(jd.size()!=0)
                {
                for(UserJoinPaymentBean up : jd) {/** Create a TableRow dynamically **/
                    i++;
                    tr = new TableRow(this);
                    if (i % 2 == 0) {
                        tr.setBackgroundColor(Color.LTGRAY);

                    }


                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));



                    /** Creating a TextView to add to the row **/

                    rowCol = new TextView(this);
                    rowCol.setText("" + i);
                    rowCol.setTextColor(Color.BLACK);
                    rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    ;
                    rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    rowCol.setPadding(5, 10, 10, 5);

                    tr.addView(rowCol);  // Adding textView to tablerow.
                    tr.setPadding(5, 10, 5, 10);
                    /** Creating another textview **/
                    String actualPay="";
                    double amount=up.getAmount();
                    int value = up.getPaymenttype();
                    if (value == 1) {
                        actualPay = "by Cash\nAmount :" + amount;
                    } else if (value == 2) {
                        actualPay = "by Cheque\nAmount :" + amount + "\nCheque Dated :" + up.getCheque_date() + "\nCheque No :" + up.getChequeno() + "\nBank Name :" + up.getBank_name();
                    }
                    else if (value == 3) {
                        actualPay = "by Netbanking\nAmount :" + amount + "\nBank Name :" + up.getBank_name() + "\nUTR No :" + up.getUtrno();
                    }
                    else if (value == 4)
                    {
                        actualPay = "by Company\nAmount :" + amount;
                    }
                    else
                    {
                        actualPay = "by Rejoining\nAmount :" + amount;
                    }



                    rowCol = new TextView(this);
                    rowCol.setText(""+actualPay);
                    rowCol.setTextColor(Color.BLACK);
                    rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    ;
                    rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    rowCol.setPadding(5, 10, 10, 5);
//            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    tr.addView(rowCol); // Adding textView to tablerow.

                    rowCol = new TextView(this);
                    rowCol.setText(""+up.getPaymentdate());

                    rowCol.setBackgroundResource(R.color.BussiPoolSecondary);
                    rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    ;
                    rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    rowCol.setPadding(5, 10, 0, 5);
//            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    tr.addView(rowCol); // Adding textView to tablerow.
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

                            TableRow.LayoutParams.FILL_PARENT,

                            TableRow.LayoutParams.WRAP_CONTENT));



                }
            }else{
                    tr = new TableRow(this);
                    rowCol = new TextView(this);
                    rowCol.setText("No data Available");

                    rowCol.setBackgroundResource(R.color.BussiPoolSecondary);
                    rowCol.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    ;
                    rowCol.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    rowCol.setPadding(5, 10, 0, 5);
                    TableRow.LayoutParams params = (TableRow.LayoutParams)rowCol.getLayoutParams();
                    params.span = 6;
                    rowCol.setLayoutParams(params);
//            valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    tr.addView(rowCol); // Adding textView to tablerow.
//
                    tl.addView(tr, new TableLayout.LayoutParams(

                            TableRow.LayoutParams.FILL_PARENT,

                            TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
            progress.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
