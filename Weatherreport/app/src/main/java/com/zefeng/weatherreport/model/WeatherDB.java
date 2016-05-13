package com.zefeng.weatherreport.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.zefeng.weatherreport.db.WeatherReportHelper;

/**
 * Created by zefeng on 2016/5/8.
 */
public class WeatherDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static WeatherDB weatherDB;
    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */
    private WeatherDB(Context context){
        WeatherReportHelper dpHelper = new WeatherReportHelper(context,DB_NAME,null,VERSION);
        db = dpHelper.getWritableDatabase();
    }

    /**
     * 获取WeatherDB的实例
     */
    public synchronized static WeatherDB getInstance(Context context){
        if (weatherDB == null){
            weatherDB = new WeatherDB(context);
        }
        return weatherDB;
    }

    /**
     * 将Province实例储存到数据库
     */
    public void saveProvince(Province province){
        if (province != null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }

    }

    /**
     * 从数据库读取全国所有省份的信息
     */

    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while(cursor.moveToNext());
        }

            return list;
    }

    /**
     * 将City实例存储到数据库中
     */
    public void saveCity(City city){
        if (city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    /**
     * 从数据库读取某省份下的所有城市信息
     */
    public List<City> loadCities(int provinceId){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City",null,"province_id = ?",new String[]{String.valueOf(provinceId)},null,null,null);
        if (cursor.moveToFirst()){
            do {
                City city = new City();
                city.setProvinceId(provinceId);
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                list.add(city);
            }while(cursor.moveToNext());
        }
        return list;
    }

    /**
     * 将Country实例存储到数据库中
     */
    public void saveCoutry(Country country){
        if (country != null){
            ContentValues values = new ContentValues();
            values.put("city_id",country.getCityId());
            values.put("country_name",country.getCountryName());
            values.put("country_code",country.getCountryCode());
            db.insert("Country",null,values);
        }
    }

    /**
     * 从数据库中获取某城市下的县信息
     */
    public List<Country> loadCountries(int cityId){
        List<Country> list = new ArrayList<>();
        Cursor cursor = db.query("Country",null,"city_id = ?",new String[]{String.valueOf(cityId)},null,null,null);
        if (cursor.moveToFirst()){
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCityId(cityId);
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                list.add(country);
            }while (cursor.moveToNext());
        }
            return list;
    }
}
