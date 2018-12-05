package com.ydkim2110.drinkshopapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Cart;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;
import com.ydkim2110.drinkshopapp.R;
import com.ydkim2110.drinkshopapp.Utils.Common;

import java.util.List;

/**
 * Created by Kim Yongdae on 2018-12-03
 * email : ydkim2110@gmail.com
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private static final String TAG = "CartAdapter";

    private Context mContext;
    private List<Cart> mCartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        mContext = context;
        mCartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        Picasso.with(mContext)
                .load(mCartList.get(position).link)
                .into(holder.img_product);
        holder.txt_amount.setNumber(String.valueOf(mCartList.get(position).amount));
        holder.txt_price.setText(new StringBuilder("$").append(mCartList.get(position).price));
        holder.txt_product_name.setText(new StringBuilder(mCartList.get(position).name)
            .append(" x")
            .append(mCartList.get(position).amount)
            .append(mCartList.get(position).size == 0 ? " Size M" : "Size L"));
        holder.txt_sugar_ice.setText(new StringBuilder("Sugar: ")
            .append(mCartList.get(position).sugar)
            .append("%")
            .append("\n")
            .append("Ice: ")
            .append(mCartList.get(position).ice)
            .append("%").toString());

        // Get Price of one cup with all options
        final double priceOneCup = mCartList.get(position).price / mCartList.get(position).amount;

        // Auto save item when user change amount
        holder.txt_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = mCartList.get(position);
                cart.amount = newValue;
                cart.price = Math.round(priceOneCup*newValue);

                Common.cartRepository.updateCart(cart);

                holder.txt_price.setText(new StringBuilder("$")
                    .append(mCartList.get(position).price));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_product;
        private TextView txt_product_name, txt_sugar_ice, txt_price;
        private ElegantNumberButton txt_amount;

        public RelativeLayout view_background;
        public LinearLayout view_foreground;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_sugar_ice = itemView.findViewById(R.id.txt_sugar_ice);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            view_background = itemView.findViewById(R.id.view_background);
            view_foreground = itemView.findViewById(R.id.view_foreground);

        }
    }

    public void removeItem(int position) {
        Log.d(TAG, "removeItem: called");
        mCartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart item, int position) {
        Log.d(TAG, "restoreItem: called");
        mCartList.add(position, item);
        notifyItemInserted(position);
    }

}
