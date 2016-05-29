package com.zefeng.pm_25;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private List<City_info> list;
    private static final int SHOW_RESPONSE = 0;
    private String httpUrl = "https://api.heweather.com/x3/weather?city=";
    private String Key = "&key=5de12a1d35ab4bb6a866ccd1c5904cec";
    private PM25db PMdb;
    private SQLiteDatabase db;
    private String idCode;

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
        PMdb = new PM25db(this, "PM2_5.db", null, 1);
        db = PMdb.getWritableDatabase();
        requestCityDB();
        send.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send) {
            cityName = input.getText().toString();
//            String id = searchCityId(cityName);
            list = LoadCityId();
            for (int i = 0; i < list.size(); i++) {
//                Log.d("ddd", list.get(i).toString());
                if (cityName.equals(list.get(i).getCity())){
                    idCode = list.get(i).getId();
                    Log.d("ddd","it work"+list.get(i).getId());
                    break;
                }
            }
//            sendRequest(cityName);
        }
    }

    /**
     * 将数据从数据库中取出，为了方便查询，将其实例化并存到list中，
     * 查询完立刻 clear list列表
     *
     * @return
     */
    private List<City_info> LoadCityId() {
        List<City_info> list2 = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City_info city_info = new City_info();
                city_info.setCity(cursor.getString(cursor.getColumnIndex("city")));
                city_info.setCnty(cursor.getString(cursor.getColumnIndex("cnty")));
                city_info.setProv(cursor.getString(cursor.getColumnIndex("provinceName")));
                city_info.setId(cursor.getString(cursor.getColumnIndex("code")));
                list2.add(city_info);
            } while (cursor.moveToNext());
        }
        return list2;
    }

    /**
     * 请求并返回城市列表
     */
    private void requestCityDB() {

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
                    Log.d("ddd", "comedbre");
                    int i = 0;
                    while ((line = reade.readLine()) != null) {
                        datas.append(line);
                        parseCityCodeJSON(datas.toString());
//                        Log.d("ddd", i++ + "");
                    }
                    //设计一个下载城市数据库的进度条，利用遍历完是的加一
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

    /**
     * 将数据存进数据库
     *
     * @param jsonData
     */
    private void parseCityCodeJSON(String jsonData) {

        try {
            Log.d("ddd", "comdCode");
            ContentValues values = new ContentValues();
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("city_info");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                values.put("city", jsonObj.getString("city"));
                values.put("cnty", jsonObj.getString("cnty"));
                values.put("code", jsonObj.getString("id"));
                values.put("provinceName", jsonObj.getString("prov"));
//              Log.d("ddd", k++ + "");
                db.insert("Province", null, values);
                values.clear();
//                saveInDB(city, cnty, id, prov);  //保存到数据库中
//                City_info city_info = new City_info();
//                city_info.setCity(jsonObject1.getString("city"));
//                city_info.setCnty(jsonObject1.getString("cnty"));
//                city_info.setId(jsonObject1.getString("id"));
//                city_info.setLat(jsonObject1.getString("lat"));
//                city_info.setLon(jsonObject1.getString("lon"));
//                city_info.setProv(jsonObject1.getString("prov"));
//                list2.add(city_info);
//                for (int j =0;j<list2.size();j++){
//                   City_info city_info1 = list2.get(j);
//                    Log.d("ddd",city_info1.toString());
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
