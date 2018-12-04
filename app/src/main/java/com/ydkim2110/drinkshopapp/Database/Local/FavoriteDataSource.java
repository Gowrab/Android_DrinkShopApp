package com.ydkim2110.drinkshopapp.Database.Local;

import com.ydkim2110.drinkshopapp.Database.DataSource.IFavoriteDataSource;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Kim Yongdae on 2018-12-04
 * email : ydkim2110@gmail.com
 */
public class FavoriteDataSource implements IFavoriteDataSource {

    private FavoriteDAO mFavoriteDAO;
    private static FavoriteDataSource instance;

    public FavoriteDataSource(FavoriteDAO favoriteDAO) {
        mFavoriteDAO = favoriteDAO;
    }

    public static FavoriteDataSource getInstance(FavoriteDAO favoriteDAO) {
        if (instance == null) {
            instance = new FavoriteDataSource(favoriteDAO);
        }
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavItems() {
        return mFavoriteDAO.getFavItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return mFavoriteDAO.isFavorite(itemId);
    }

    @Override
    public void delete(Favorite favorite) {
        mFavoriteDAO.delete(favorite);
    }
}
