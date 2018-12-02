package com.ydkim2110.drinkshopapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;
import com.ydkim2110.drinkshopapp.Interface.IItemClickListener;
import com.ydkim2110.drinkshopapp.Model.Drink;
import com.ydkim2110.drinkshopapp.R;

import java.util.List;

/**
 * Created by Kim Yongdae on 2018-12-02
 * email : ydkim2110@gmail.com
 */
public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {

    private static final String TAG = "DrinkAdapter";

    private Context mContext;
    private List<Drink> mDrinkList;

    public DrinkAdapter(Context context, List<Drink> drinkList) {
        mContext = context;
        mDrinkList = drinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.drink_item_layout, parent, false);

        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, final int position) {

        holder.txt_price.setText(new StringBuilder("$").append(mDrinkList.get(position).Price));
        holder.txt_drink_name.setText(mDrinkList.get(position).Name);

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddToCartDialog(position);
            }
        });

        Picasso.with(mContext)
                .load(mDrinkList.get(position).Link)
                .into(holder.img_product);

        holder.setIItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddToCartDialog(int position) {
        Log.d(TAG, "showAddToCartDialog: called");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.add_to_cart_layout, null);

        // View
        ImageView img_product_dialog = view.findViewById(R.id.img_cart_product);
        ElegantNumberButton txt_count = view.findViewById(R.id.txt_count);
        TextView txt_product_dialog = view.findViewById(R.id.txt_cart_product_name);

        EditText edt_comment = view.findViewById(R.id.edt_comment);

        RadioButton rdi_sizeM = view.findViewById(R.id.rdi_sizeM);
        RadioButton rdi_sizeL = view.findViewById(R.id.rdi_sizeL);

        RadioButton rdi_sugar_100 = view.findViewById(R.id.rdi_sugar_100);
        RadioButton rdi_sugar_70 = view.findViewById(R.id.rdi_sugar_70);
        RadioButton rdi_sugar_50 = view.findViewById(R.id.rdi_sugar_50);
        RadioButton rdi_sugar_30 = view.findViewById(R.id.rdi_sugar_30);
        RadioButton rdi_sugar_free = view.findViewById(R.id.rdi_sugar_free);

        RadioButton rdi_ice_100 = view.findViewById(R.id.rdi_ice_100);
        RadioButton rdi_ice_70 = view.findViewById(R.id.rdi_ice_70);
        RadioButton rdi_ice_50 = view.findViewById(R.id.rdi_ice_50);
        RadioButton rdi_ice_30 = view.findViewById(R.id.rdi_ice_30);
        RadioButton rdi_ice_free = view.findViewById(R.id.rdi_ice_free);

        // set data
        Picasso.with(mContext)
                .load(mDrinkList.get(position).Link)
                .into(img_product_dialog);

        builder.setView(view);
        builder.setNegativeButton("ADD TO CART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return mDrinkList.size();
    }
}
