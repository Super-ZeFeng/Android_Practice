package com.zefeng.pm_25.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by zefeng on 2016/5/26.
 */
public class PM25db extends SQLiteOpenHelper{

    public static final String CREATE_CityList = "create table Province("
            +"id integer primary key autoincrement,"
            +"provinceName text,"
            +"cnty text,"
            +"code integer)";
    private Context mContext;

    public PM25db(Context context, String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CityList);
        Toast.makeText(mContext, "created successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
