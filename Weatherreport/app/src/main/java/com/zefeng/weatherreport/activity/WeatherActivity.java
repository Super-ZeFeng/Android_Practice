package com.zefeng.weatherreport.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.zefeng.weatherreport.R;
import com.zefeng.weatherreport.util.HttpCallbackListener;
import com.zefeng.weatherreport.util.HttpUtil;
import com.zefeng.weatherreport.util.Utility;

/**
 * Created by zefeng on 2016/5/11.
 */
public class WeatherActivity extends Activity implements OnClickListener {
    private LinearLayout weatherInFoLayout;
    /**
     * 用于显示城市名
     */
    private TextView cityNameText;
    /**
     * 用于显示发布时间
     */
    private TextView publishText;
    /**
     * 用于显示天气描述信息
     */
    private TextView weatherDespText;
    /**
     * 用于显示气温1
     */
    private TextView temp1Text;
    /**
     * 用于显示气温2
     */
    private TextView temp2Text;
    /**
     * 用于显示当前日期
     */
    private  TextView currentDateText;
    /**
     * 切换城市按钮
     */
    private Button switchCity;
    /**
     * 更新天气按钮
     */
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        //初始化控件
        weatherInFoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_data);
        switchCity = (Button) findViewById(R.id.switch_city);
        refreshWeather = (Button) findViewById(R.id.flesh_weather);
        String countryCode = getIntent().getStringExtra("country_code");
        if (!TextUtils.isEmpty(countryCode)){
            //有县级代号时就去查询天气
            publishText.setText("同步中....");
            weatherInFoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
                                                         Log.d("aaa",7+"");
            queryWeatherCode(countryCode);
        }else {
            //没有县级代号时就直接显示本地天气
            showWeather();
        }
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
    }

    /**
     * 查询天气代号所对应的天气
     */
    private void queryWeatherInFo(String weatherCode){

                    Log.d("aaa",4+""+weatherCode);

        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
                          //http://www.weather.com.cn/data/cityinfo/
        queryFromServer(address,"weatherCode");
    }

    /**
     * 查询县级代号所对应的天气代号
     */
    private void queryWeatherCode(String countryCode) {
                                                Log.d("aaa",8+"");

        String address = "http://www.weather.com.cn/data/list3/city"+countryCode+".xml";
        queryFromServer(address,"countryCode");
    }

    /**
     * 从SharedPreferences文件中读取储存的天气信息，并显示到界面上
     */
    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        cityNameText.setText(prefs.getString("city_name",""));
        temp1Text.setText(prefs.getString("temp1",""));
        temp2Text.setText(prefs.getString("temp2",""));
        weatherDespText.setText(prefs.getString("weather_desp",""));
        publishText.setText("今天"+prefs.getString("publish_time","")+"发布");
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInFoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
     * @param address
     * @param type
     */
    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("countryCode".equals(type)){
                                                      Log.d("aaa",5+" 0.0.0"+response);

                    if (!TextUtils.isEmpty(response)){
                        //从服务器中返回的数据中解析出天气代号
                        String[] array = response.split("\\|");
                                                       Log.d("aaa",9+"   0.0.0  "+array.length);
                        if (array!=null && array.length==2){
                            String weatherCode = array[1];
                            queryWeatherInFo(weatherCode);
                        }
                    }
                }else if ("weatherCode".equals(type)){
                    //处理服务器返回的天气信息
                                                         Log.d("aaa",6+"0.0.0");

                    Utility.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_city:
                Intent intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.flesh_weather:
                publishText.setText("同步中....");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("weather_code","");
                if (!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInFo(weatherCode);
                }
                break;
            default:
                break;

        }
    }
}