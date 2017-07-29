package com.yyyclj.facescore.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by 姚尧 on 2017/4/8.
 * okhttp工具类
 */

public class OkHttpClientMgr {
    private static OkHttpClientMgr mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private static final String TAG = "OkHttpClientMgr";

    private OkHttpClientMgr() {
        mOkHttpClient = new OkHttpClient.Builder()
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(15, TimeUnit.SECONDS)
                                .writeTimeout(15, TimeUnit.SECONDS)
                                .build();
        mDelivery = new Handler(Looper.getMainLooper());

    }

    public static OkHttpClientMgr getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientMgr.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientMgr();
                }
            }
        }
        return mInstance;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    private Response _getAsyn(String url) throws IOException
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private void _getAsyn(String url, final ResultCallback callback)
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    private Response _post(String url, Param... params) throws IOException
    {
        Request request = buildPostRequest(url, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }


    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return 字符串
     */
    private String _postAsString(String url, Param... params) throws IOException
    {
        Response response = _post(url, params);
        return response.body().string();
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Param... params)
    {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params)
    {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException
    {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }


    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException
     */
    private void _postAsyn(final String url, final ResultCallback callback, final File file, final String fileKey, final Param... params) throws IOException
    {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    /**
     * 上传内存中的bitmap
     * @param url
     * @param callback
     * @param bitmaps
     * @param fileKeys
     * @param fileNames
     * @param params
     * @throws IOException
     */
    private void _postAsyn(final String url, final ResultCallback callback, final Bitmap[] bitmaps, final String[] fileKeys, String[] fileNames, final Param... params) throws IOException
    {
        Request request = buildMultipartFormRequest(url, bitmaps, fileKeys, fileNames, params);
        deliveryResult(callback, request);
    }


    private void setErrorResId(final ImageView view, final int errorResId)
    {
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                view.setImageResource(errorResId);
            }
        });
    }

    /**
     * 从下载链接中获取文件名称
     *
     * @param path
     */
    private String _getFileName(String path)
    {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }


    //*************对外公布的方法************

    public static void getAsyn(String url, ResultCallback callback) {
        getInstance()._getAsyn(url, callback);
    }

    public static Response post(String url, Param... params) throws IOException
    {
        return getInstance()._post(url, params);
    }

    public static String postAsString(String url, Param... params) throws IOException
    {
        return getInstance()._postAsString(url, params);
    }

    public static void postAsyn(String url, final ResultCallback callback, Param... params)
    {
        getInstance()._postAsyn(url, callback, params);
    }

    public static void postAsyn(String url, final ResultCallback callback, Map<String, String> params)
    {
        getInstance()._postAsyn(url, callback, params);
    }

    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException
    {
        getInstance()._postAsyn(url, callback, file, fileKey);
    }

    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException
    {
        getInstance()._postAsyn(url, callback, file, fileKey, params);
    }

    public static void postAsyn(String url, ResultCallback callback, Bitmap bitmap, String fileKey, String fileName, Param... params) throws IOException
    {
        getInstance()._postAsyn(url, callback, new Bitmap[]{bitmap}, new String[]{fileKey}, new String[]{fileName}, params);
    }

    public static String getFileName(String path) {
        return getInstance()._getFileName(path);
    }

    //****************************

    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params) {
        params = validateParam(params);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }

        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private Request buildMultipartFormRequest(String url, Bitmap[] bitmaps,
                                              String[] fileKeys, String[] fileNames, Param[] params) {
        params = validateParam(params);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }

        if (bitmaps != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < bitmaps.length; i++) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmaps[i].compress(Bitmap.CompressFormat.JPEG, 100, bos);

                fileBody = RequestBody.create(MediaType.parse("image/png"), bos.toByteArray());
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileNames[i] + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private String inputStream2String(InputStream is){
        BufferedReader in = new BufferedReader(new InputStreamReader(is));

        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static String readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        String data = outStream.toString();
        outStream.close();
        inStream.close();
        return data;

    }

    private Param[] validateParam(Param[] params)
    {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params)
    {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries)
        {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private void deliveryResult(final ResultCallback callback, final Request request)
    {
        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    if (response.isSuccessful()){
                        Headers headers = response.headers();
                        Log.i("TAG", "headers: " + headers);
                    }

                    final String string = response.body().string();
                    sendSuccessResultCallback(string, callback);

                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }

            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback)
    {
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        //在主线程运行
        mDelivery.post(new Runnable() {
            @Override
            public void run()
            {
                if (callback != null)
                {
                    callback.onResponse(object);
                }
            }
        });
    }

    //post请求，这里加上cookie.使服务器识别已经登录
    private Request buildPostRequest(String url, Param[] params)
    {
        if (params == null)
        {
            params = new Param[0];
        }
        //FormEncodingBuilder已被FormBody取代
        FormBody.Builder builder = new FormBody.Builder();
        String body = "";
        for (Param param : params)
        {
            body = body + "&" + param.key + "=" + param.value;
        }
        //解决默认编码格式为UTF-8 的方式
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=gb2312"), body);

        return new Request.Builder()
                .url(url)
                .addHeader("contentType", "GB2312")
                .addHeader("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 5.1; MX5 Build/LMY47I)")
                .addHeader("Host", "bbs.nju.edu.cn")
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();
    }


    public static abstract class ResultCallback<T>
    {
        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);

    }

    public static class Param
    {
        public Param()
        {
        }

        public Param(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }


}
