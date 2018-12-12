package com.ydkim2110.drinkshopapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ydkim2110.drinkshopapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView mOrderId, mOrderPrice, mOrderAddress, mOrderComment, mOrderStatus;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        mOrderId = itemView.findViewById(R.id.txt_order_id);
        mOrderPrice = itemView.findViewById(R.id.txt_order_price);
        mOrderAddress = itemView.findViewById(R.id.txt_order_address);
        mOrderComment = itemView.findViewById(R.id.txt_order_comment);
        mOrderStatus = itemView.findViewById(R.id.txt_order_status);
    }
}
