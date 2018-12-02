package com.ydkim2110.drinkshopapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.ydkim2110.drinkshopapp.Model.Category;
import com.ydkim2110.drinkshopapp.R;

import java.util.List;

/**
 * Created by Kim Yongdae on 2018-12-02
 * email : ydkim2110@gmail.com
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private static final String TAG = "CategoryAdapter";

    private Context mContext;
    private List<Category> mCategoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        mContext = context;
        mCategoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.menu_item_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Load Image
        Picasso.with(mContext)
                .load(mCategoryList.get(position).Link)
                .into(holder.img_product);

        holder.txt_menu_name.setText(mCategoryList.get(position).Name);
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }
}
