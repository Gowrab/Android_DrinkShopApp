package com.ydkim2110.drinkshopapp.Database.DataSource;

import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Kim Yongdae on 2018-12-04
 * email : ydkim2110@gmail.com
 */
public class FavoriteRepository  implements IFavoriteDataSource {

    private static final String TAG = "FavoriteRepository";

    private IFavoriteDataSource mIFavoriteDataSource;

    public FavoriteRepository(IFavoriteDataSource IFavoriteDataSource) {
        mIFavoriteDataSource = IFavoriteDataSource;
    }

    private static FavoriteRepository instance;
    public static FavoriteRepository getInstance(IFavoriteDataSource favoriteDataSource) {
        if (instance == null) {
            instance = new FavoriteRepository(favoriteDataSource);
        }
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavItems() {
        return mIFavoriteDataSource.getFavItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return mIFavoriteDataSource.isFavorite(itemId);
    }

    @Override
    public void delete(Favorite favorite) {
        mIFavoriteDataSource.delete(favorite);
    }
}
