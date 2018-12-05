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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ydkim2110.drinkshopapp.Adapter.CartAdapter;
import com.ydkim2110.drinkshopapp.Adapter.FavoriteAdapter;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Cart;
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

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private static final String TAG = "CartActivity";

    private RecyclerView recycler_cart;
    CartAdapter cartAdapter;
    private Button btn_place_order;

    private List<Cart> mCartList = new ArrayList<>();
    private RelativeLayout mRootLayout;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Log.d(TAG, "onCreate: started");

        mCompositeDisposable = new CompositeDisposable();

        recycler_cart = findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerITemTouchHelper(
                0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        btn_place_order = findViewById(R.id.btn_place_order);

        mRootLayout = findViewById(R.id.rootLayout);

        loadCartItems();
    }

    private void loadCartItems() {
        Log.d(TAG, "loadCartItems: called");
        mCompositeDisposable.add(Common.cartRepository.getCartItems()
            .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<List<Cart>>() {
            @Override
            public void accept(List<Cart> carts) throws Exception {
                displayCartItem(carts);
            }
        }));
    }

    private void displayCartItem(List<Cart> carts) {
        mCartList = carts;
        cartAdapter = new CartAdapter(this, carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        mCompositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder) {
            String name = mCartList.get(viewHolder.getAdapterPosition()).name;

            final Cart deletedItem = mCartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // delete item from adapter
            cartAdapter.removeItem(deletedIndex);

            // delete item from room database
            Common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(mRootLayout,
                    new StringBuilder(name).append(" removed from Favorites List").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreItem(deletedItem, deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


}
