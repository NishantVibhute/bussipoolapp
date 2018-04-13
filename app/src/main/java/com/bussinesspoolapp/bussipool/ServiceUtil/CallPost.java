package com.bussinesspoolapp.bussipool.ServiceUtil;

import android.content.Context;
import android.os.AsyncTask;


import com.bussinesspoolapp.bussipool.Util.CommonUtil;
import com.bussinesspoolapp.bussipool.interfaces.AsyncResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nishant on 2/27/2018.
 */

public class CallPost extends AsyncTask<String, Void, Void> {
    public AsyncResponse delegate = null;
    private Context context;
String content;
    public CallPost(AsyncResponse context)
    {
        this.delegate = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        BufferedReader reader=null;
// Send data
        try
        {


            // Defined URL  where to send data
            URL url = new URL(CommonUtil.url+"/MarketingService/webresources"+params[0]);

            // Send POST data request

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","text/plain");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( params[1] );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "");
            }

            // Append Server Response To Content String
            content = sb.toString();

        }
        catch(Exception ex)
        {
            ex.getMessage();
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {
                ex.getStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {


        delegate.processFinish(content);
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

}
