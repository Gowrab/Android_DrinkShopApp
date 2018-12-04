package com.ydkim2110.drinkshopapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;
import com.ydkim2110.drinkshopapp.R;

import java.util.List;

/**
 * Created by Kim Yongdae on 2018-12-04
 * email : ydkim2110@gmail.com
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private static final String TAG = "FavoriteAdapter";

    private Context mContext;
    private List<Favorite> mFavoriteList;

    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        mContext = context;
        mFavoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.fav_item_layout, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Picasso.with(mContext)
                .load(mFavoriteList.get(position).link)
                .into(holder.img_product);

        holder.txt_price.setText(new StringBuilder("$")
                .append(mFavoriteList.get(position).price).toString());

        holder.txt_product_name.setText(mFavoriteList.get(position).name);

    }

    @Override
    public int getItemCount() {
        return mFavoriteList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_product;
        private TextView txt_product_name, txt_price;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_price = itemView.findViewById(R.id.txt_price);
        }
    }
}
