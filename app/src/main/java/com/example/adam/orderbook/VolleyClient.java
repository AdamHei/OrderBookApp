package com.example.adam.orderbook;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyClient {
    private static VolleyClient mClient;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private VolleyClient(Context context) {
        mContext = context;
        mRequestQueue = getmRequestQueue();
    }

    @NonNull
    public static synchronized VolleyClient getInstance(Context context) {
        if (mClient == null) {
            mClient = new VolleyClient(context);
        }
        return mClient;
    }

    @NonNull
    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getmRequestQueue().add(request);
    }
}
