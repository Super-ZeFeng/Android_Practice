package com.zefeng.weatherreport.util;

/**
 * Created by zefeng on 2016/5/8.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
