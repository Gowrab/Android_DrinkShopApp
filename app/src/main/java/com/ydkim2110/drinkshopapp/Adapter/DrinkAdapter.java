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

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Cart;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;
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
    public void onBindViewHolder(@NonNull final DrinkViewHolder holder, final int position) {

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
        
        // Favorite System
        if (Common.favoriteRepository.isFavorite(Integer.parseInt(mDrinkList.get(position).ID)) == 1) {
            Log.d(TAG, "onBindViewHolder: isFavorite true");
            holder.btn_favorite.setImageResource(R.drawable.ic_favorite_white_24dp);
        }
        else {
            Log.d(TAG, "onBindViewHolder: isFavorite false");
            holder.btn_favorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }

        holder.btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "isFavorite: " + Common.favoriteRepository.isFavorite(Integer.parseInt(mDrinkList.get(position).ID)));
                if (Common.favoriteRepository.isFavorite(Integer.parseInt(mDrinkList.get(position).ID)) != 1) {
                    addOrRemoveFavorite(mDrinkList.get(position), true);
                    holder.btn_favorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                }
                else {
                    addOrRemoveFavorite(mDrinkList.get(position), false);
                    holder.btn_favorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }
            }
        });

    }

    private void addOrRemoveFavorite(Drink drink, boolean isAdd) {
        Log.d(TAG, "addOrRemoveFavorite: called");

        Favorite favorite = new Favorite();
        favorite.id = drink.ID;
        favorite.link = drink.Link;
        favorite.name = drink.Name;
        favorite.price = drink.Price;
        favorite.menuId = drink.MenuId;

        if (isAdd) {
            Log.d(TAG, "addOrRemoveFavorite: insert");
            Common.favoriteRepository.insertFav(favorite);
        }
        else {
            Log.d(TAG, "addOrRemoveFavorite: delete");
            Common.favoriteRepository.delete(favorite);
        }

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

                showConfirmDialog(position, txt_count.getNumber());
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showConfirmDialog(final int position, final String number) {
        Log.d(TAG, "showConfirmDialog: called");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.confirm_add_to_cart_dialog, null);

        // View
        ImageView img_product_dialog = view.findViewById(R.id.img_product);
        final TextView txt_product_dialog = view.findViewById(R.id.txt_cart_product_name);
        TextView txt_product_price = view.findViewById(R.id.txt_cart_product_price);
        TextView txt_sugar = view.findViewById(R.id.txt_sugar);
        TextView txt_ice = view.findViewById(R.id.txt_ice);
        final TextView txt_topping_extra = view.findViewById(R.id.txt_topping_extra);

        // set data
        Picasso.with(mContext)
                .load(mDrinkList.get(position).Link)
                .into(img_product_dialog);
        txt_product_dialog.setText(new StringBuilder(mDrinkList.get(position).Name)
                .append(" x")
                .append(Common.sizeOfCup == 0 ? " Size M" : " Size L")
                .append(number).toString());

        txt_ice.setText(new StringBuilder("Ice: ")
                .append(Common.ice)
                .append("%").toString());
        txt_sugar.setText(new StringBuilder("Sugar: ")
                .append(Common.sugar)
                .append("%").toString());

        double price = (Double.parseDouble(mDrinkList.get(position).Price) * Double.parseDouble(number))
                + Common.toppingPrice;

        if (Common.sizeOfCup == 1) { // size L
            price += (3.0*Double.parseDouble(number));
        }

        StringBuilder topping_final_comment = new StringBuilder("");
        for (String  line : Common.toppingAdded) {
            topping_final_comment.append(line).append("\n");
        }

        txt_topping_extra.setText(topping_final_comment);

        final double finalPrice = Math.round(price);

        txt_product_price.setText(new StringBuilder("$").append(finalPrice));

        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                try {
                    // Add to SQLite
                    // create new Cart item
                    Cart cartItem = new Cart();
                    cartItem.name = mDrinkList.get(position).Name;
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.ice = Common.ice;
                    cartItem.sugar = Common.sugar;
                    cartItem.price = finalPrice;
                    cartItem.size = Common.sizeOfCup;
                    cartItem.toppingExtras = txt_topping_extra.getText().toString();
                    cartItem.link = mDrinkList.get(position).Link;

                    // Add to DB
                    Common.cartRepository.insertToCart(cartItem);

                    Log.d(TAG, "DEBUG: " + new Gson().toJson(cartItem));

                    Toast.makeText(mContext, " 카트에 저장했습니다.", Toast.LENGTH_SHORT).show();
                    // Implement in next part
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
