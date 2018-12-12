package com.ydkim2110.drinkshopapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ydkim2110.drinkshopapp.Model.Order;
import com.ydkim2110.drinkshopapp.R;
import com.ydkim2110.drinkshopapp.Utils.Common;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private static final String TAG = "OrderAdapter";

    private Context mContext;
    private List<Order> mOrderList;

    public OrderAdapter(Context mContext, List<Order> mOrderList) {
        this.mContext = mContext;
        this.mOrderList = mOrderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Log.d(TAG, "onCreateViewHolder: called");

        View orderView = LayoutInflater.from(mContext).inflate(R.layout.order_layout, parent, false);
        return new OrderViewHolder(orderView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.mOrderId.setText(new StringBuilder("#").append(mOrderList.get(position).getOrderId()));
        holder.mOrderPrice.setText(new StringBuilder("$").append(mOrderList.get(position).getOrderPrice()));
        holder.mOrderAddress.setText(mOrderList.get(position).getOrderAddress());
        holder.mOrderComment.setText(mOrderList.get(position).getOrderComment());
        holder.mOrderStatus.setText(new StringBuilder("Order Status: ")
                .append(Common.convertCodeToStatus(mOrderList.get(position).getOrderStatus())));
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
}
