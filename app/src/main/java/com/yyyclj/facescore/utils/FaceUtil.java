package com.yyyclj.facescore.utils;

import android.graphics.Bitmap;

import java.io.IOException;

/**
 * Created by 姚尧 on 2017/7/28.
 */

public class FaceUtil {

    public static void dectect(final Bitmap bitmap, final String fileName, final OkHttpClientMgr.ResultCallback callBack)
    {
        //因为这里要向网络发送数据是耗时操作所以要在新线程中执行
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClientMgr.Param key = new OkHttpClientMgr.Param("api_key", Constants.Key);
                OkHttpClientMgr.Param secret = new OkHttpClientMgr.Param("api_secret", Constants.Secret);
                OkHttpClientMgr.Param landmark = new OkHttpClientMgr.Param("return_landmark", "" + 1);
                OkHttpClientMgr.Param attributes = new OkHttpClientMgr.Param("return_attributes", Constants.ATTRIBUTES);

                try {
                    OkHttpClientMgr.postAsyn(Constants.URL_DETECT, callBack, bitmap, Constants.IMG_FILE, fileName,
                           new OkHttpClientMgr.Param[]{key, secret, landmark, attributes});
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
