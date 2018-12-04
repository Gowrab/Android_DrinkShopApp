package com.ydkim2110.drinkshopapp;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.ydkim2110.drinkshopapp.Adapter.FavoriteAdapter;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;
import com.ydkim2110.drinkshopapp.Utils.Common;
import com.ydkim2110.drinkshopapp.Utils.RecyclerITemTouchHelper;
import com.ydkim2110.drinkshopapp.Utils.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoriteListActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private static final String TAG = "FavoriteListActivity";

    private RecyclerView mRecyclerViewFav;

    private RelativeLayout mRootLayout;
    private CompositeDisposable mCompositeDisposable;
    private FavoriteAdapter mFavoriteAdapter;
    private List<Favorite> mLocalFavorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        Log.d(TAG, "onCreate: started");

        mCompositeDisposable = new CompositeDisposable();

        mRootLayout = findViewById(R.id.rootLayout);

        mRecyclerViewFav = findViewById(R.id.recycler_fav);
        mRecyclerViewFav.setHasFixedSize(true);
        mRecyclerViewFav.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerITemTouchHelper(
                0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecyclerViewFav);

        loadFavoriteItem();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
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
        mLocalFavorites = favorites;
        mFavoriteAdapter = new FavoriteAdapter(this, favorites);
        mRecyclerViewFav.setAdapter(mFavoriteAdapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof FavoriteAdapter.FavoriteViewHolder) {
            String name = mLocalFavorites.get(viewHolder.getAdapterPosition()).name;

            final Favorite deletedItem = mLocalFavorites.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // delete item from adapter
            mFavoriteAdapter.removeItem(deletedIndex);

            // delete item from room database
            Common.favoriteRepository.delete(deletedItem);

            Snackbar snackbar = Snackbar.make(mRootLayout,
                    new StringBuilder(name).append(" removed from Favorites List").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFavoriteAdapter.restoreItem(deletedItem, deletedIndex);
                    Common.favoriteRepository.insertFav(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
