package com.bussinesspoolapp.bussipool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beans.UserBean;
import com.beans.UserPassword;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class Login extends AppCompatActivity  implements View.OnClickListener,AsyncResponse {
    TextView txtSignUp ,txtError;
    EditText username,password;
    Button butSignIn;

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private static final String PREF_IsValid = "isValid";


    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtSignUp = findViewById(R.id.txtSignUp);
        txtError = findViewById(R.id.txtErrorMsg);
        butSignIn = findViewById(R.id.butSignIn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        butSignIn.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        String isval = settings.getString(PREF_IsValid,"notvalid");
        boolean isAllow=false;
        if(isval.equalsIgnoreCase("isValid"))
        {
            Intent myIntent1 = new Intent(Login.this, Home.class);
            Login.this.finish();
            Login.this.startActivity(myIntent1);

        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.txtSignUp:
                Intent myIntent = new Intent(Login.this, SignUp.class);
                Login.this.finish();
                this.startActivity(myIntent);
                break;
            case R.id.butSignIn:

                UnameValue = ""+username.getText();
                PasswordValue = ""+password.getText();


                if(UnameValue.equalsIgnoreCase(""))
                {
                    txtError.setText("Please Enter EmailId");
                }else if(PasswordValue.equalsIgnoreCase(""))
            {
                txtError.setText("Please Enter Password");
            }
            else{
                CallPost cp = new CallPost(this);
                UserPassword up = new UserPassword();
                up.setEmailId(UnameValue);
                up.setPassword(PasswordValue);

                try {
                    String jsonInString = objectMapper.writeValueAsString(up);
                    cp.execute("/user/validate",jsonInString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }








        }

    }

    private void savePreferences(UserBean up) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
//        UnameValue = ""+username.getText();
//        PasswordValue = ""+password.getText();
        System.out.println("onPause save name: " + UnameValue);
        System.out.println("onPause save password: " + PasswordValue);
        editor.putString(PREF_UNAME, up.getFirstName());
        editor.putLong("userId", up.getId());
        editor.putString("fname", up.getFirstName());
        editor.putString("lname", up.getLastName());
        editor.putString("emailid", up.getEmailId());
        editor.putString(PREF_IsValid,"isValid");
        editor.commit();
    }


    @Override
    public void processFinish(String output) {
        boolean isAllow=false;
        try {
            UserBean up = objectMapper.readValue(output,UserBean.class);
            if(up.getEmailId()!=null)
            {
                isAllow=true;
                savePreferences(up);
            }else{
                txtError.setText("Invalid UserId and Password");
            }


            if(isAllow) {

                Intent myIntent1 = new Intent(Login.this, Home.class);
                Login.this.finish();
                this.startActivity(myIntent1);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
