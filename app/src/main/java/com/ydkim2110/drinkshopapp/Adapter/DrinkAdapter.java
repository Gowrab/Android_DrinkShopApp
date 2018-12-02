package com.ydkim2110.drinkshopapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
import com.ydkim2110.drinkshopapp.Utils.Common;

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

    private void showAddToCartDialog(final int position) {
        Log.d(TAG, "showAddToCartDialog: called");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.add_to_cart_layout, null);

        // View
        ImageView img_product_dialog = view.findViewById(R.id.img_cart_product);
        final ElegantNumberButton txt_count = view.findViewById(R.id.txt_count);
        TextView txt_product_dialog = view.findViewById(R.id.txt_cart_product_name);

        EditText edt_comment = view.findViewById(R.id.edt_comment);

        RadioButton rdi_sizeM = view.findViewById(R.id.rdi_sizeM);
        RadioButton rdi_sizeL = view.findViewById(R.id.rdi_sizeL);

        rdi_sizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Common.sizeOfCup = 0;
                }
            }
        });
        rdi_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Common.sizeOfCup = 1;
                }
            }
        });

        RadioButton rdi_sugar_100 = view.findViewById(R.id.rdi_sugar_100);
        RadioButton rdi_sugar_70 = view.findViewById(R.id.rdi_sugar_70);
        RadioButton rdi_sugar_50 = view.findViewById(R.id.rdi_sugar_50);
        RadioButton rdi_sugar_30 = view.findViewById(R.id.rdi_sugar_30);
        RadioButton rdi_sugar_free = view.findViewById(R.id.rdi_sugar_free);

        rdi_sugar_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar = 30;
            }
        });

        rdi_sugar_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar = 50;
            }
        });

        rdi_sugar_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar = 70;
            }
        });

        rdi_sugar_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar = 100;
            }
        });

        rdi_sugar_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar = 0;
            }
        });

        RadioButton rdi_ice_100 = view.findViewById(R.id.rdi_ice_100);
        RadioButton rdi_ice_70 = view.findViewById(R.id.rdi_ice_70);
        RadioButton rdi_ice_50 = view.findViewById(R.id.rdi_ice_50);
        RadioButton rdi_ice_30 = view.findViewById(R.id.rdi_ice_30);
        RadioButton rdi_ice_free = view.findViewById(R.id.rdi_ice_free);

        rdi_ice_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.ice = 30;
            }
        });

        rdi_ice_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.ice = 50;
            }
        });

        rdi_ice_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.ice = 70;
            }
        });

        rdi_ice_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.ice = 100;
            }
        });

        rdi_ice_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.ice = 0;
            }
        });

        RecyclerView recycler_topping = view.findViewById(R.id.recycler_topping);
        recycler_topping.setLayoutManager(new LinearLayoutManager(mContext));
        recycler_topping.setHasFixedSize(true);

        MultiChoiceAdapter adapter = new MultiChoiceAdapter(mContext, Common.toppingList);
        recycler_topping.setAdapter(adapter);

        // set data
        Picasso.with(mContext)
                .load(mDrinkList.get(position).Link)
                .into(img_product_dialog);

        builder.setView(view);
        builder.setPositiveButton("ADD TO CART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Common.sizeOfCup == -1) {
                    Toast.makeText(mContext, "컵 사이즈를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Common.sugar == -1) {
                    Toast.makeText(mContext, "설탕 양을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Common.ice == -1) {
                    Toast.makeText(mContext, "아이스 양을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                showConfirmDialog(position, txt_count.getNumber(), Common.sizeOfCup, Common.sugar, Common.ice);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showConfirmDialog(int position, String number, int sizeOfCup, int sugar, int ice) {
        Log.d(TAG, "showConfirmDialog: called");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.confirm_add_to_cart_dialog, null);

        // View
        ImageView img_product_dialog = view.findViewById(R.id.img_product);
        TextView txt_product_dialog = view.findViewById(R.id.txt_cart_product_name);
        TextView txt_product_price = view.findViewById(R.id.txt_cart_product_price);
        TextView txt_sugar = view.findViewById(R.id.txt_sugar);
        TextView txt_ice = view.findViewById(R.id.txt_ice);
        TextView txt_topping_extra = view.findViewById(R.id.txt_topping_extra);

        // set data
        Picasso.with(mContext)
                .load(mDrinkList.get(position).Link)
                .into(img_product_dialog);
        txt_product_dialog.setText(new StringBuilder(mDrinkList.get(position).Name)
                .append(" x")
                .append(number)
                .append(Common.sizeOfCup == 0 ? " Size M" : " Size L").toString());

        txt_ice.setText(new StringBuilder("Ice: ")
                .append(Common.ice)
                .append("%").toString());
        txt_sugar.setText(new StringBuilder("Sugar: ")
                .append(Common.sugar)
                .append("%").toString());

        double price = (Double.parseDouble(mDrinkList.get(position).Price) * Double.parseDouble(number))
                + Common.toppingPice;

        if (Common.sizeOfCup == 1) {
            price += 3.0;
        }

        txt_product_price.setText(new StringBuilder("$").append(price));

        StringBuilder topping_final_comment = new StringBuilder("");
        for (String  line : Common.toppingAdded) {
            topping_final_comment.append(line).append("\n");
        }

        txt_topping_extra.setText(topping_final_comment);

        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Add to SQLite
                // Implement in next part
                dialogInterface.dismiss();
            }
        });
        builder.setView(view);
        builder.show();


    }

    @Override
    public int getItemCount() {
        return mDrinkList.size();
    }
}
