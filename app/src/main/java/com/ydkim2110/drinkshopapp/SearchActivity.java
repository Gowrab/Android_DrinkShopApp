package com.ydkim2110.drinkshopapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.ydkim2110.drinkshopapp.Adapter.DrinkAdapter;
import com.ydkim2110.drinkshopapp.Model.Drink;
import com.ydkim2110.drinkshopapp.Retrofit.IDrinkShopAPI;
import com.ydkim2110.drinkshopapp.Utils.Common;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private List<String> suggestList = new ArrayList<>();
    private List<Drink> localDataSource = new ArrayList<>();
    private MaterialSearchBar mSearchBar;

    private IDrinkShopAPI mService;

    private RecyclerView mRecyclerViewSearch;
    private DrinkAdapter mSearchAdapter, mAdapter;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "onCreate: started");

        // init service
        mService = Common.getAPI();

        mRecyclerViewSearch = findViewById(R.id.recycler_search);
        mRecyclerViewSearch.setLayoutManager(new GridLayoutManager(this, 2));

        mSearchBar = findViewById(R.id.searchBar);
        mSearchBar.setHint("Enter your drink");
        
        loadAllDrinks();

        mSearchBar.setCardViewElevation(10);
        mSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<>();

                for (String search : suggestList) {
                    if (search.toLowerCase().contains(mSearchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }

                mSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled) {
                    // Restore full list of drink
                    mRecyclerViewSearch.setAdapter(mAdapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        Log.d(TAG, "startSearch: called");
        List<Drink> result = new ArrayList<>();
        for (Drink drink : localDataSource) {
            if (drink.Name.contains(text)) {
                result.add(drink);
            }
        }
        mSearchAdapter = new DrinkAdapter(this, result);
        mRecyclerViewSearch.setAdapter(mSearchAdapter);
    }

    @Override
    protected void onStop() {
        mCompositeDisposable.clear();
        super.onStop();
    }

    private void loadAllDrinks() {
        Log.d(TAG, "loadAllDrinks: called");

        mCompositeDisposable.add(mService.getAllDrinks()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<List<Drink>>() {
                @Override
                public void accept(List<Drink> drinks) throws Exception {
                    displayListDrink(drinks);
                    buildSuggestList(drinks);
            }
        }));
    }

    private void buildSuggestList(List<Drink> drinks) {
        Log.d(TAG, "buildSuggestList: called");

        for (Drink drink : drinks) {
            suggestList.add(drink.Name);
        }
        mSearchBar.setLastSuggestions(suggestList);
    }

    private void displayListDrink(List<Drink> drinks) {
        Log.d(TAG, "displayListDrink: called");

        localDataSource = drinks;
        mAdapter = new DrinkAdapter(this, drinks);
        mRecyclerViewSearch.setAdapter(mAdapter);
    }
}
