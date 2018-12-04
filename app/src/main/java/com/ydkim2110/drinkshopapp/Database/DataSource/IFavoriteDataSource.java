package com.ydkim2110.drinkshopapp.Database.DataSource;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;

import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Kim Yongdae on 2018-12-04
 * email : ydkim2110@gmail.com
 */
public interface IFavoriteDataSource {

    Flowable<List<Favorite>> getFavItems();

    int isFavorite(int itemId);

    void insertFav(Favorite...favorites);

    void delete(Favorite favorite);

}
