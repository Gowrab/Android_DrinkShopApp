package com.ydkim2110.drinkshopapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ydkim2110.drinkshopapp.Adapter.OrderAdapter;
import com.ydkim2110.drinkshopapp.Model.Order;
import com.ydkim2110.drinkshopapp.Retrofit.IDrinkShopAPI;
import com.ydkim2110.drinkshopapp.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ShowOrderActivity extends AppCompatActivity {

    private static final String TAG = "ShowOrderActivity";

    private IDrinkShopAPI mService;
    private RecyclerView mRecyclerViewOrders;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        Log.d(TAG, "onCreate: started");

        mService = Common.getAPI();

        mRecyclerViewOrders = findViewById(R.id.recycler_orders);
        mRecyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewOrders.setHasFixedSize(true);

        loadOrder();
    }

    private void loadOrder() {
        Log.d(TAG, "loadOrder: called");

        if (Common.currentUser != null) {
            mCompositeDisposable.add(mService.getOrder(Common.currentUser.getPhone(), "0")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<Order>>() {
                        @Override
                        public void accept(List<Order> orders) throws Exception {
                            displayOrder(orders);
                        }
                    }));
        }
        else {
            Toast.makeText(this, "로그인 정보가 없습니다. 다시 로그인 해주세요.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void displayOrder(List<Order> orders) {
        Log.d(TAG, "displayOrder: called");

        OrderAdapter adapter =  new OrderAdapter(this, orders);
        mRecyclerViewOrders.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        loadOrder();
        super.onResume();
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
