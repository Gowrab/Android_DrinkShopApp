package com.ydkim2110.drinkshopapp.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ydkim2110.drinkshopapp.Database.ModelDB.Cart;

/**
 * Created by Kim Yongdae on 2018-12-02
 * email : ydkim2110@gmail.com
 */
@Database(entities = {Cart.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    private static CartDatabase instance;

    public static CartDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, CartDatabase.class, "DrinkShopDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
