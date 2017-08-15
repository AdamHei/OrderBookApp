package com.example.adam.orderbook;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class GeminiInterface {

    static void getLastPrice(final Context context, final TextView lastPriceView) {
        String url = "https://api.gemini.com/v1/pubticker/btcusd";
        JsonObjectRequest tickerRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    BigDecimal lastPrice = new BigDecimal(response.getString("last"));
                    lastPriceView.setText(String.format(context.getResources().getString(R.string.dollar_amount), lastPrice.toString()));
                } catch (JSONException e) {
                    displayErrorMessage("Could not parse ticker response", context);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayErrorMessage(error.getMessage(), context);
            }
        });
        VolleyClient.getInstance(context).addToRequestQueue(tickerRequest);
    }

    static void requestBidsAndAsks(final Context context,
                                   final OrdersAdapter bidsAdapter,
                                   final OrdersAdapter asksAdapter,
                                   final TextView spread) {
        String url = "https://api.gemini.com/v1/book/btcusd";
        JsonObjectRequest dataRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateDataModels(response, bidsAdapter, asksAdapter, spread, context.getResources());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayErrorMessage(error.getMessage(), context);
            }
        });
        VolleyClient.getInstance(context).addToRequestQueue(dataRequest);
    }

    private static void updateDataModels(JSONObject data,
                                         OrdersAdapter bidsAdapter,
                                         OrdersAdapter asksAdapter,
                                         TextView spread,
                                         Resources resources) {
        try {
            JSONArray bids = data.getJSONArray("bids");
            JSONArray asks = data.getJSONArray("asks");

            List<Order> bidsList = new ArrayList<>(50);
            List<Order> asksList = new ArrayList<>(50);

            for (int i = 0; i < bids.length(); i++) {
                JSONObject order = bids.getJSONObject(i);
                bidsList.add(new Order("BID",
                        new BigDecimal(order.getString("amount")),
                        new BigDecimal(order.getString("price"))));
            }

            for (int i = 0; i < asks.length(); i++) {
                JSONObject order = asks.getJSONObject(i);
                asksList.add(new Order("ASK",
                        new BigDecimal(order.getString("amount")),
                        new BigDecimal(order.getString("price"))));
            }

            BigDecimal spreadAmount = asksList.get(0).price.subtract(bidsList.get(0).price);
            spread.setText(String.format(resources.getString(R.string.dollar_amount), spreadAmount.toString()));

            bidsAdapter.setOrders(bidsList);
            asksAdapter.setOrders(asksList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void displayErrorMessage(String msg, Context context) {
        Log.e("GeminiInterface", msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
