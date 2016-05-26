package com.zefeng.pm_25;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button send;
    private TextView textView;
    private static  final int SHOW_RESPONSE = 0;
    private String httpUrl = "http://apis.baidu.com/apistore/aqiservice/citylist";
    private String httpArg = "";
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String respone = (String) msg.obj;
                    textView.setText(respone);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = (Button) findViewById(R.id.send);
        textView = (TextView) findViewById(R.id.textView);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send){
            sendRequest();
        }
    }

    private void sendRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(httpUrl+"?"+httpArg);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("apikey","9e99797682748f85e5d82cdab919f537");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"Unicode"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!= null){
                        response.append(line);
//                        response.append("\r\n");
                    }
                        Log.d("ddd",response.toString());
                    in.close();
                    reader.close();
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = response.toString();
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
