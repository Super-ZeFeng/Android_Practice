package com.zefeng.weatherreport.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.zefeng.weatherreport.model.City;
import com.zefeng.weatherreport.model.Country;
import com.zefeng.weatherreport.model.Province;
import com.zefeng.weatherreport.model.WeatherDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zefeng on 2016/5/8.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvinceResponse(WeatherDB weatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allPronvinces = response.split(",");
            if (allPronvinces != null && allPronvinces.length > 0) {
                for (String p : allPronvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析出来的数据储存到Province表中
                    weatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(WeatherDB weatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String p:allCities){
                    String[] array = p.split("\\|");
                    City city = new City();
                    city.setProvinceId(provinceId);
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    //将解析出来的数据储存到City表中
                    weatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */

    public static boolean handleCountryResponse(WeatherDB weatherDB,String response,int CityId){
        if (!TextUtils.isEmpty(response)){
            String[] allCountry = response.split(",");
            if (allCountry!=null && allCountry.length>0){
                for (String p : allCountry){
                    String[] array = p.split("\\|");
                    Country country = new Country();
                    country.setCountryCode(array[0]);
                    country.setCountryName(array[1]);
                    country.setCityId(CityId);
                    //将解析出来的数据储存到Country表中
                    weatherDB.saveCoutry(country);
                }
                return true;
            }
        }
        return false;
    }
    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
     */
    public static void handleWeatherResponse(Context context,String response){
        try {

                     Log.d("aaa",3+""+response);

            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            Log.d("ccc",cityName);
            String weatherCode = weatherInfo.getString("cityid");
            Log.d("ccc",weatherCode);
            String temp1 = weatherInfo.getString("temp1");
            Log.d("ccc",temp1);
            String temp2 = weatherInfo.getString("temp2");
            Log.d("ccc",temp2);
            String weatherDesp = weatherInfo.getString("weather");
            Log.d("ccc",weatherDesp);
            String publishTime = weatherInfo.getString("ptime");
            Log.d("ccc",publishTime);
            Log.d("aaa",10+""+cityName+weatherCode+temp1+temp2+weatherDesp+publishTime);
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharePreferences文件中
     */
    public static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1,
                                       String temp2, String weatherDesp, String publishTime) {

                  Log.d("aaa",2+"");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        Log.d("ccc",sdf.format(new Date()));
        editor.commit();
    }
}
