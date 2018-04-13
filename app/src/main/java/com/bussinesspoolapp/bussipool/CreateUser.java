package com.bussinesspoolapp.bussipool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beans.BankDetailsBean;
import com.beans.SchemeJoinBean;
import com.beans.UserBean;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.regex.Pattern;

public class CreateUser extends AppCompatActivity implements View.OnClickListener,AsyncResponse {

    private static final String PREFS_NAME = "preferences";
    Button save;
    ObjectMapper objectMapper = new ObjectMapper();
    int schemeId,payId;
    String type="";

    EditText fname,mlname,lname,phone,address,pan,aadhar,bankname,ifsc,branch,account;
    TextView email;
    UserBean up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String emailId= settings.getString("emailid","");
        Intent intent = getIntent();
        schemeId = intent.getExtras().getInt("schemeId");
        payId = intent.getExtras().getInt("payId");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        fname=findViewById(R.id.fname);
        mlname= findViewById(R.id.mname);
        lname = findViewById(R.id.lnam);
        email=findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        pan = findViewById(R.id.pan);
        aadhar = findViewById(R.id.aadhar);
        bankname = findViewById(R.id.bankName);
        ifsc = findViewById(R.id.ifscCode);
        branch = findViewById(R.id.branchName);
        account = findViewById(R.id.bankAccountNo);
        email.setText(emailId);
        save = findViewById(R.id.saveUser);
        save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String fname1 = ""+fname.getText();

        String lname1 = ""+lname.getText();
        String phone1 = ""+phone.getText();
        String address1 = ""+address.getText();
        String pan1 = ""+pan.getText();
        String aadhar1 = ""+aadhar.getText();


        if(fname1.equals(""))
        {
            fname.setHintTextColor(Color.RED);
            fname.setError("Please enter first name");
        }
        else if(lname1.equals(""))
        {
            lname.setHintTextColor(Color.RED);
            lname.setError("Please enter last name");
        }else if(Pattern.matches("[a-zA-Z]+", phone1)) {

            phone.setHintTextColor(Color.RED);
            phone.setError("Please enter Digits");

        }else if(phone.length() < 10 ||phone.length() > 10) {
            // if(phone.length() != 10) {
            phone.setHintTextColor(Color.RED);
            phone.setError("Not Valid Number");
        }else if(address1.equals(""))
        {
            address.setHintTextColor(Color.RED);
            address.setError("Please enter Address");
        }
        else if(pan1.equals(""))
        {
            pan.setHintTextColor(Color.RED);
            pan.setError("Please enter Pan Card No");
        }
        else if(aadhar1.equals(""))
        {
            aadhar.setHintTextColor(Color.RED);
            aadhar.setError("Please enter Addhar Card No");
        }
else {

            CallPost cp = new CallPost(this);
             up = new UserBean();
            up.setFirstName(fname.getText() + "");
            up.setMiddleName(mlname.getText() + "");
            up.setLastName(lname.getText() + "");
            up.setEmailId(email.getText() + "");
            up.setMobileNo(phone.getText() + "");
            up.setAddress(address.getText() + "");
            up.setPanCardNo(pan.getText() + "");
            up.setAadharCardNo(aadhar.getText() + "");
            BankDetailsBean bk = new BankDetailsBean();
            bk.setBankName(bankname.getText() + "");
            bk.setIfscCode(ifsc.getText() + "");
            bk.setBranchName(branch.getText() + "");
            bk.setBankAccNo(account.getText() + "");
            up.setBankDetails(bk);
            try {
                String val = objectMapper.writeValueAsString(up);
                type = "user";
                cp.execute("/user/create", val);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void processFinish(String output) {
if(type.equals("user")){
            int userId = Integer.parseInt(output);
            if (userId != 0) {
                savePreferences(userId);
                SchemeJoinBean sj = new SchemeJoinBean();
                sj.setUserId((int) userId);
                sj.setSchemeId(schemeId);
                sj.setPaymentModeId(payId);
                sj.setUserStatus(1);
                sj.setMemberType(1);
                try {
                    String a = objectMapper.writeValueAsString(sj);
                    CallPost cp = new CallPost(this);
                    type="join";

                    cp.execute("/scheme/join", a);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Failed To save details", Toast.LENGTH_LONG);
            }
        }else{
            Intent i = new Intent(CreateUser.this,Home.class);
            startActivity(i);
            CreateUser.this.finish();
        }
    }
    private void savePreferences(int userId) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
//        UnameValue = ""+username.getText();
//        PasswordValue = ""+password.getText();

        editor.putString("Username", up.getFirstName());
        editor.putLong("userId", userId);
        editor.putString("fname", up.getFirstName());
        editor.putString("lname", up.getLastName());
        editor.putString("emailid", up.getEmailId());
        editor.putString("isValid","isValid");
        editor.commit();
    }
}
