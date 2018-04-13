package com.bussinesspoolapp.bussipool;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beans.MessageBean;
import com.beans.SentMessageBean;
import com.beans.TemplateBean;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.Util.CommonUtil;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Promote extends AppCompatActivity implements View.OnClickListener,AsyncResponse {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    final int CONTACT_PICK_REQUEST = 1000;
    TextView butSelect  ;
    public static final int REQUEST_CODE_PICK_CONTACT = 1;
    public static final int  MAX_PICK_CONTACT= 10;
    TextView txtContactCount;
    EditText txtMessage;
    Button butSendPromotion;
    ObjectMapper objectMapper = new ObjectMapper();
    List<String> numbers = new ArrayList<>();
    List<String> names = new ArrayList<>();
    int selectedContactsCount=0;
    int schemeId;
    long userId;
    String type="";
    ProgressDialog progress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote);
        schemeId= CommonUtil.getSessionInt(this,"schemeId");

        userId= CommonUtil.getSessionLong(this,"userId");
        butSelect = findViewById(R.id.selectContacts);
        butSelect.setOnClickListener(this);
        butSendPromotion = findViewById(R.id.butSendPromotion);
        butSendPromotion.setOnClickListener(this);
        txtContactCount = findViewById(R.id.txtContactCount);
        txtMessage = findViewById(R.id.txtMessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        CallPost cp = new CallPost(this);
        type="promotionalMsg";
        TemplateBean tc = new TemplateBean();
        tc.setSchemId(schemeId);
        tc.setTemplate("Promotion");
        try {
            String req = objectMapper.writeValueAsString(tc);
            cp.execute("/message/getSMSTemplateContent",req);
        } catch (IOException e) {
            e.printStackTrace();
        }

        progress = new ProgressDialog(this);

        progress.setTitle("Loading");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted



            } else {
                Toast.makeText(this, "Until you grant the permission, we can not display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectContacts:
                Intent intentContactPick = new Intent(Promote.this,ContactsPickerActivity.class);
                Promote.this.startActivityForResult(intentContactPick,CONTACT_PICK_REQUEST);
                break;
            case R.id.butSendPromotion:
                SentMessageBean sentMessageBean = new SentMessageBean();
                sentMessageBean.setBulkTo(numbers);
                sentMessageBean.setMessage(""+txtMessage.getText());
                sentMessageBean.setFrom(userId);
                sentMessageBean.setSchemeId(schemeId);
                sentMessageBean.setTempId(5);
                try {
                    String val = objectMapper.writeValueAsString(sentMessageBean);
                    type="SendMsg";
                    CallPost cp =new CallPost(this);
                    cp.execute("/message/sendBulkSMS",val);
                    progress.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    @Override
    public void processFinish(String output) {
        try {
            if(type.equalsIgnoreCase("sendMsg"))
            {

                progress.dismiss();
    Toast.makeText(this,"SMS Sent Successfully",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Promote.this,SchemeHome.class);
                this.startActivity(intent);
            }else {
                MessageBean messageContent = objectMapper.readValue(output, MessageBean.class);
                txtMessage.setText(messageContent.getBody());

                progress.dismiss();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_OK){

            ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");

            String display="";

            for(int i=0;i<selectedContacts.size();i++){

                numbers.add(selectedContacts.get(i).phone);
                names.add(selectedContacts.get(i).name);

// display += (i+1)+". "+selectedContacts.get(i).toString()+"\n";
                selectedContactsCount++;
            }
            txtContactCount.setText(selectedContactsCount+" Contacts Selected");

        }

    }
}
