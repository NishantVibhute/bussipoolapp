package com.bussinesspoolapp.bussipool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.beans.SchemeBean;
import com.bussinesspoolapp.bussipool.Adapter.CustomListAdapter;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallGet;
import com.bussinesspoolapp.bussipool.ServiceUtil.CallPost;
import com.bussinesspoolapp.bussipool.Util.CommonUtil;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity  implements AsyncResponse{

    ImageView image;
    ObjectMapper objectMapper = new ObjectMapper();
    String type="";

    ProgressDialog mProgressDialog;

    List<Bitmap> imgid1 = new ArrayList<Bitmap>();
    ListView list;
    int[] itemId;
    int[] itemCount;
    String[] itemname;
    List<SchemeBean> schemeBeanList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(CommonUtil.isNetworkAvailable(this)) {

        list=(ListView)findViewById(R.id.list);

//        new DownloadImage(this).execute(URL);

        mProgressDialog = new ProgressDialog(Home.this);
        // Set progressdialog title
        mProgressDialog.setTitle("");
        // Set progressdialog message
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setIndeterminate(false);

        CallGet cp = new CallGet(this);
        try {
            mProgressDialog.show();
            cp.execute("/scheme/getlist");
            type="schemeList";
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }




        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // TODO Auto-generated method stub
                int Slecteditem= itemId[+position];

                String SlecteditemName= itemname[+position];
                Intent i = new Intent(Home.this,SchemeHome.class);
//                i.putExtra("schemeId", Slecteditem);
//                i.putExtra("schemeName",SlecteditemName);
                CommonUtil.setSessionVariable(Home.this,"schemeId",""+Slecteditem,"int");
                CommonUtil.setSessionVariable(Home.this,"schemeName",""+SlecteditemName,"String");

//                SharedPreferences settings = getSharedPreferences("preferences",
//                        Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putInt("schemeId", Slecteditem);
//                editor.putString("schemeName", SlecteditemName);
                startActivity(i);
//                Toast.makeText(v getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
            }else{
                Toast.makeText(this,"No internet connection",Toast.LENGTH_LONG).show();
            }

    }

    @Override
    public void processFinish(String output) {
        if(type.equals("schemeList"))
        {
            try {

                int i=0;
                List<SchemeBean> schemeBeanList = objectMapper.readValue(output,new TypeReference<List<SchemeBean>>(){});
                String[] urls= new String[schemeBeanList.size()];
                itemId = new int[schemeBeanList.size()];
                itemCount =new int[schemeBeanList.size()];
                itemname = new String[schemeBeanList.size()];
                for(SchemeBean s : schemeBeanList){
                    itemId[i]=s.getId();
                    itemname[i]=s.getSchemeName();
                    itemCount[i]=s.getJoinCount();
                    urls[i]=CommonUtil.url+"/MarketingService/webresources/scheme/getLogo/"+s.getSchemeName();
                    i++;
                }

                new DownloadImage(this).execute(urls);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, List<Bitmap>> {

        private Activity mContext;

        public DownloadImage (Activity context){
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<Bitmap> doInBackground(String... URL) {
            int count = URL.length;
            //URL url = urls[0];

            List<Bitmap> bitmaps = new ArrayList<>();
            for(int i=0;i<count;i++){
            String imageURL = URL[i];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                bitmaps.add(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
            }
            return bitmaps;
        }

        @Override
        protected void onPostExecute(List<Bitmap> result) {
            for(int i=0;i<result.size();i++) {
                imgid1.add(result.get(i));
            }

            Bitmap[] imgid= imgid1.toArray(new Bitmap[imgid1.size()]);
            CustomListAdapter adapter=new CustomListAdapter(mContext, itemname, imgid,itemCount);

            list.setAdapter(adapter);
            mProgressDialog.dismiss();

        }
    }



}
