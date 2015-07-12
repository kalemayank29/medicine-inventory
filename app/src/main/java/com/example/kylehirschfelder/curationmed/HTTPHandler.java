package com.example.kylehirschfelder.curationmed;

import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class HTTPHandler {

    public String httpString;
    public List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();

    public HTTPHandler(String httpString, List<NameValuePair> NameValuePairs){
        this.httpString = httpString;
        this.NameValuePairs = NameValuePairs;
    }

    public boolean requestHTTP(){

        InputStream is;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(httpString);
            httpPost.setEntity(new UrlEncodedFormEntity(NameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (ClientProtocolException e) {
            Log.e("ClientProtocol", "log_tag");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("log_tag", "IOException");
            e.printStackTrace();
        }
        return true;
    }
}
