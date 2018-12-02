package com.ydkim2110.drinkshopapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ydkim2110.drinkshopapp.R;

/**
 * Created by Kim Yongdae on 2018-12-02
 * email : ydkim2110@gmail.com
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public ImageView img_product;
    public TextView txt_menu_name;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        img_product = itemView.findViewById(R.id.image_product);
        txt_menu_name = itemView.findViewById(R.id.txt_menu_name);
    }
}
