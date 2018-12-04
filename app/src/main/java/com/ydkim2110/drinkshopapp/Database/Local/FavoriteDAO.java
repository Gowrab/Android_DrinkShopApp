package com.ydkim2110.drinkshopapp.Database.Local;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;

import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Kim Yongdae on 2018-12-04
 * email : ydkim2110@gmail.com
 */
public interface FavoriteDAO {

    @Query("SELECT * FROM Favorite")
    Flowable<List<Favorite>> getFavItems();

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite WHERE id=:itemId)")
    int isFavorite(int itemId);

    @Delete
    void delete(Favorite favorite);

}
