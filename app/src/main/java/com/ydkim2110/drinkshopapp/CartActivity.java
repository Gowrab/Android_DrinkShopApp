package com.ydkim2110.drinkshopapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.ydkim2110.drinkshopapp.Adapter.CartAdapter;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Cart;
import com.ydkim2110.drinkshopapp.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";

    private RecyclerView recycler_cart;
    private Button btn_place_order;

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

        btn_place_order = findViewById(R.id.btn_place_order);

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
        CartAdapter cartAdapter = new CartAdapter(this, carts);
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
}
