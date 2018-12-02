package com.ydkim2110.drinkshopapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ydkim2110.drinkshopapp.Interface.IItemClickListener;
import com.ydkim2110.drinkshopapp.R;

/**
 * Created by Kim Yongdae on 2018-12-02
 * email : ydkim2110@gmail.com
 */
public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView img_product;
    public TextView txt_drink_name, txt_price;

    IItemClickListener mIItemClickListener;

    public Button btn_add_to_cart;

    public void setIItemClickListener(IItemClickListener IItemClickListener) {
        mIItemClickListener = IItemClickListener;
    }

    public DrinkViewHolder(@NonNull View itemView) {
        super(itemView);

        img_product = itemView.findViewById(R.id.image_product);
        txt_drink_name = itemView.findViewById(R.id.txt_drink_name);
        txt_price = itemView.findViewById(R.id.txt_price);
        btn_add_to_cart = itemView.findViewById(R.id.btn_add_to_cart);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mIItemClickListener.onClick(view);
    }
}
