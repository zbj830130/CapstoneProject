package com.bojinzhang.android.Util;

import android.annotation.SuppressLint;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhangbojin on 23/05/17.
 */

public class HttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @SuppressLint("NewApi")
    public static Response sendHttpRequest(String address) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        return client.newCall(request).execute();
    }

    public static void sendHttpRequestAsyn(final String address, okhttp3.Callback callback) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static boolean postJson(String json, String address) {
        boolean result = true;
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            result = new JSONObject(data).getBoolean("Result");

        } catch (Exception e) {
        }

        return result;

    }
}
