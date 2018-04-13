package com.bussinesspoolapp.bussipool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beans.UserBean;
import com.beans.UserPassword;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallGet;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener,AsyncResponse {

    EditText edtUserName,edtPassword,edtConfPassword ;
    Button butSignUp;
    ObjectMapper objectMapper = new ObjectMapper();
    ProgressDialog progress ;
    TextView txtError;
    String type="";
    List<String> userEmailList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfPassword = findViewById(R.id.edtConfPassword);
        butSignUp = findViewById(R.id.butSignUp);
        butSignUp.setOnClickListener(this);
        progress = new ProgressDialog(this);
        txtError = findViewById(R.id.txtSignUpError);

        progress.setTitle("Loading");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);

        CallGet cp = new CallGet(this);
        progress.show();
        cp.execute("/user/getuserdetailslist");
    type="userList";



    }

    @Override
    public void onClick(View v) {




        String email = ""+edtUserName.getText();
        String pass = ""+edtPassword.getText();
        String confPass = ""+edtConfPassword.getText();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(email.equalsIgnoreCase(""))
        {
            txtError.setText("Please Enter Email Id");
        }else if(!email.matches(emailPattern)) {
            txtError.setText("Please Enter valid Email Id");
        }else if(pass.equalsIgnoreCase("")){
            txtError.setText("Please Enter Password");
        }
        else if(pass.length()<4){
            txtError.setText("Password must contains atleast 4 characters");
        }
        else if(confPass.equalsIgnoreCase("")){
            txtError.setText("Please enter Retype Password");
        }else if(!pass.equals(confPass)){
            txtError.setText("Password doesn't matched");
        }else {
            if (userEmailList.contains(email)) {
                txtError.setText("User Already Exists");
            } else {
                try {
                    UserPassword up = new UserPassword();
                    up.setEmailId(email);
                    up.setPassword(pass);
                    String data = objectMapper.writeValueAsString(up);
                    CallPost cp = new CallPost(this);
                    progress.show();
    type="createUser";
                    cp.execute("/user/signup", data);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Override
    public void processFinish(String output) {
        if(type.equalsIgnoreCase("userList")){
            List<UserBean> userList = null;
            try {
                userList = objectMapper.readValue(output, new TypeReference<List<UserBean>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (UserBean ub : userList) {
                userEmailList.add(ub.getEmailId());
            }
        }else {
            Toast.makeText(this, output,
                    Toast.LENGTH_LONG).show();
            Intent myIntent1 = new Intent(SignUp.this, Login.class);
            SignUp.this.finish();
            SignUp.this.startActivity(myIntent1);
        }
        progress.dismiss();

    }

    @Override
    public void onBackPressed() {
        Intent myIntent1 = new Intent(SignUp.this, Login.class);
        SignUp.this.finish();
        SignUp.this.startActivity(myIntent1);
    }
}

