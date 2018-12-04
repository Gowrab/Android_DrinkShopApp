package com.ydkim2110.drinkshopapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ydkim2110.drinkshopapp.Adapter.DrinkAdapter;
import com.ydkim2110.drinkshopapp.Model.Drink;
import com.ydkim2110.drinkshopapp.Retrofit.IDrinkShopAPI;
import com.ydkim2110.drinkshopapp.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DrinkActivity extends AppCompatActivity {

    private static final String TAG = "DrinkActivity";

    private IDrinkShopAPI mService;

    private RecyclerView lst_drink;

    private TextView txt_banner_name;

    // Rxjava
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        Log.d(TAG, "onCreate: started");

        mService = Common.getAPI();

        lst_drink = findViewById(R.id.recycler_drinks);
        lst_drink.setLayoutManager(new GridLayoutManager(this, 2));
        lst_drink.setHasFixedSize(true);

        txt_banner_name = findViewById(R.id.txt_menu_name);
        txt_banner_name.setText(Common.currentCategory.Name);

        loadListDrink(Common.currentCategory.ID);

    }

    private void loadListDrink(String menuId) {
        mCompositeDisposable.add(mService.getDrink(menuId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Drink>>() {
            @Override
            public void accept(List<Drink> drinks) throws Exception {
                displayDrinkList(drinks);
            }
        }));
    }

    private void displayDrinkList(List<Drink> drinks) {
        DrinkAdapter adapter = new DrinkAdapter(this, drinks);
        lst_drink.setAdapter(adapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
