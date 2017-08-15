package com.example.adam.orderbook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private List<Order> mOrders;
    private Context mContext;

    OrdersAdapter(@NonNull Context context) {
        this.mContext = context;
        this.mOrders = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.order, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = mOrders.get(position);

        holder.typeView.setText(order.type);
        holder.amountView.setText(order.amount.toString());
        holder.priceView.setText(
                String.format(mContext.getResources().getString(R.string.dollar_amount), order.price.toString()));
    }

    public void setOrders(@NonNull List<Order> orders) {
        this.mOrders.clear();
        this.mOrders.addAll(orders);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView typeView, amountView, priceView;

        ViewHolder(View itemView) {
            super(itemView);

            typeView = itemView.findViewById(R.id.typeTextView);
            amountView = itemView.findViewById(R.id.amountTextView);
            priceView = itemView.findViewById(R.id.priceTextView);
        }
    }
}
