package com.temoa.startor.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Temoa
 * on 2016/9/2 16:43
 *
 * 自定义GSON请求体
 */
public class GsonRequest<T> extends Request<T> {

    private Gson mGson = new Gson();
    private Listener<T> mListener;
    private Class<T> mClass;

    /**
     * 创建一个默认GET的请求
     * @param url           目标链接
     * @param clazz         beans
     * @param listener      请求成功监听
     * @param errorListener 请求错误监听
     */
    public GsonRequest(String url, Class<T> clazz,
                       Listener<T> listener, ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);

    }

    /**
     * 创建一个请求
     * @param method        请求方法
     * @param url           目标链接
     * @param clazz         beans
     * @param listener      请求成功监听
     * @param errorListener 请求错误监听
     */
    public GsonRequest(int method, String url, Class<T> clazz,
                       Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mClass = clazz;
    }

    /**
     * 处理返回信息
     * @param response 返回信息
     * @return GSON解析后的Json信息
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        try {
//            return Response.success(mGson.fromJson(parsed, mClass), HttpHeaderParser.parseCacheHeaders(response));
            return Response.success(mGson.fromJson(parsed, mClass), cache(response, 60));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 设置Header
     * @return Header
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36");
        return headers;
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        mListener = null;
    }

    /**
     * 强制缓存
     * @param response 接收体
     * @param maxAge 缓存时间
     */
    private static Cache.Entry cache(NetworkResponse response, long maxAge) {
        long now = System.currentTimeMillis();
        if (maxAge == 0)
            maxAge = 60;
        Map<String, String> headers = response.headers;

        long serverDate = 0;
        long softExpire = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        softExpire = now + maxAge * 1000;
        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = entry.softTtl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;
        return entry;
    }
}
