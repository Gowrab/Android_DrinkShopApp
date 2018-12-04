package com.ydkim2110.drinkshopapp.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ydkim2110.drinkshopapp.Database.ModelDB.Cart;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;

/**
 * Created by Kim Yongdae on 2018-12-02
 * email : ydkim2110@gmail.com
 */
@Database(entities = {Cart.class, Favorite.class}, version = 1)
public abstract class YDKIMRoomDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();

    private static YDKIMRoomDatabase instance;

    public static YDKIMRoomDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, YDKIMRoomDatabase.class, "DrinkShopDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
