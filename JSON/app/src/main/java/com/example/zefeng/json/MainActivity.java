package com.example.zefeng.json;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button send;
    private static final String TAG = "Json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = (Button) findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "you clicked");
                requestUrl();
            }
        });
    }

    private void requestUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("https://api.heweather.com/x3/citylist?search=allchina&key=5de12a1d35ab4bb6a866ccd1c5904cec");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    Log.d(TAG,"start");
                    InputStream in = connection.getInputStream();

                    BufferedReader response = new BufferedReader(new InputStreamReader(in));
                    StringBuilder str = new StringBuilder();
                    String line;
                    while ((line = response.readLine())!= null){
                        str.append(line);
                        Log.d(TAG, str.toString());
                        JsonGetBack(str.toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (connection !=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void JsonGetBack(String JsonData) {
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("city_info");
            for (int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String city = jsonObject1.getString("city");
                Log.d(TAG,city);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
