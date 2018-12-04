package com.ydkim2110.drinkshopapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.ydkim2110.drinkshopapp.Adapter.FavoriteAdapter;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;
import com.ydkim2110.drinkshopapp.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoriteListActivity extends AppCompatActivity {

    private static final String TAG = "FavoriteListActivity";

    private RecyclerView recycler_fav;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        Log.d(TAG, "onCreate: started");

        mCompositeDisposable = new CompositeDisposable();

        recycler_fav = findViewById(R.id.recycler_fav);
        recycler_fav.setHasFixedSize(true);
        recycler_fav.setLayoutManager(new LinearLayoutManager(this));

        loadFavoriteItem();

    }

    private void loadFavoriteItem() {
        Log.d(TAG, "loadFavoriteItem: called");

        mCompositeDisposable.add(Common.favoriteRepository.getFavItems()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<List<Favorite>>() {
                @Override
                public void accept(List<Favorite> favorites) throws Exception {
                    displayFavoriteItem(favorites);
                }
        }));
    }

    private void displayFavoriteItem(List<Favorite> favorites) {
        Log.d(TAG, "displayFavoriteItem: called");
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(this, favorites);
        recycler_fav.setAdapter(favoriteAdapter);
    }
}
