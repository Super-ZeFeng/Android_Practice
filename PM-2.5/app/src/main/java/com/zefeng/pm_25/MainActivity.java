package com.zefeng.pm_25;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zefeng.pm_25.cityobj.City_info;
import com.zefeng.pm_25.db.PM25db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button send;
    private TextView textView;
    private EditText input;
    private String cityName;
    private String temp;
    private List<City_info> list;
    private static final int SHOW_RESPONSE = 0;
    private String httpUrl = "https://api.heweather.com/x3/weather?city=";
    private String Key = "&key=5de12a1d35ab4bb6a866ccd1c5904cec";
    private PM25db db;
//    private String httpUrl = "http://apis.baidu.com/apistore/aqiservice/citylist";
//    private String httpArg = "";

    //    private Handler handler = new Handler(){
//        public void handleMessage(Message msg){
//            switch (msg.what) {
//                case SHOW_RESPONSE:
//                    String respone = (String) msg.obj;
//                    textView.setText(respone);
//            }
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = (Button) findViewById(R.id.send);
        textView = (TextView) findViewById(R.id.textView);
        input = (EditText) findViewById(R.id.input);
        db = new PM25db(this, "PM2_5.db", null, 1);
        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send) {
//            cityName = input.getText().toString();
            requestJSON();
//            db.getWritableDatabase();
//            sendRequest(cityName);
        }
    }

    private void requestJSON() {
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
                    InputStream in = connection.getInputStream();

                    BufferedReader reade = new BufferedReader(new InputStreamReader(in));
                    StringBuilder datas = new StringBuilder();
                    String line;
                    int i = 0;
                    while ((line = reade.readLine()) != null) {
                        datas.append(line);
//                        datas.append("\r\n");
                        temp = datas.toString();
//                        Log.d("ddd", temp);
                       list = parseJSON(temp);
                        Log.d("ddd", "" + list);
//                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private List<City_info> parseJSON(String jsonData) {
        try {
            List<City_info> list2 = new ArrayList<>();
            Log.d("ddd", "start");
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject json = new JSONObject("status");
            String ok = json.getString("ok");
            //获取jsonData Key 对应的 value
            JSONArray jsonArray = jsonObject.getJSONArray("city_info");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                City_info city_info = new City_info();
                city_info.setCity(jsonObject1.getString("city"));
                city_info.setCnty(jsonObject1.getString("cnty"));
                city_info.setId(jsonObject1.getString("id"));
                city_info.setLat(jsonObject1.getString("lat"));
                city_info.setLon(jsonObject1.getString("lon"));
                city_info.setProv(jsonObject1.getString("prov"));
//                Log.d("ddd",city_info.getCity()+"..."+city_info.getCnty()+"..."+city_info.getCnty()+"..."+
//                        city_info.getId()+"..."+city_info.getProv()+"...");
                list2.add(city_info);
                for (int j =0;j<list2.size();j++){
                   City_info city_info1 = list2.get(j);
                    Log.d("ddd",city_info1.toString());
                }
            }

//            Log.d("ddd",city+"..."+id+"..."+prov);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

//    private void sendRequest(final String cityName) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                try {
//                    URL url = new URL("https://api.heweather.com/x3/citylist?search=allchina&key=5de12a1d35ab4bb6a866ccd1c5904cec");
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
////                    connection.setRequestProperty("apikey","9e99797682748f85e5d82cdab919f537");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    InputStream in = connection.getInputStream();
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine())!= null){
//                        response.append(line);
//                        response.append("\r\n");
//                    }
//                    Message message = new Message();
//                    message.what = SHOW_RESPONSE;
//                    message.obj = response.toString();
//                    handler.sendMessage(message);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }finally {
//                    if (connection != null){
//                        connection.disconnect();
//                    }
//                }
//            }
//        }).start();
//    }
}
