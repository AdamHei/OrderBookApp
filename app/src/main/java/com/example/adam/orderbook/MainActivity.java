package com.example.adam.orderbook;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Context context;
    OrdersAdapter bidsAdapter, asksAdapter;
    TextView spreadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupAdaptersAndViews();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GeminiInterface.requestBidsAndAsks(context, bidsAdapter, asksAdapter, spreadView);
            }
        }, 0, 500);
    }

    private void setupAdaptersAndViews() {
        context = this;
        spreadView = (TextView) findViewById(R.id.spread);
        final TextView lastpriceView = (TextView) findViewById(R.id.lastprice);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeminiInterface.requestBidsAndAsks(context, bidsAdapter, asksAdapter, spreadView);
                GeminiInterface.getLastPrice(context, lastpriceView);
            }
        });

        bidsAdapter = new OrdersAdapter(this);
        asksAdapter = new OrdersAdapter(this);

        RecyclerView asksOrderList = (RecyclerView) findViewById(R.id.asks_order_list);
        asksOrderList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        asksOrderList.setAdapter(asksAdapter);

        RecyclerView bidsOrderList = (RecyclerView) findViewById(R.id.bids_order_list);
        bidsOrderList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bidsOrderList.setAdapter(bidsAdapter);

        GeminiInterface.requestBidsAndAsks(this, bidsAdapter, asksAdapter, spreadView);
        GeminiInterface.getLastPrice(context, lastpriceView);
    }
}
