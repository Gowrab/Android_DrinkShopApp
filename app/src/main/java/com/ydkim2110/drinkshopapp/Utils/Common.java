package com.ydkim2110.drinkshopapp.Utils;

import com.ydkim2110.drinkshopapp.Database.DataSource.CartRepository;
import com.ydkim2110.drinkshopapp.Database.Local.CartDatabase;
import com.ydkim2110.drinkshopapp.Model.Category;
import com.ydkim2110.drinkshopapp.Model.Drink;
import com.ydkim2110.drinkshopapp.Model.User;
import com.ydkim2110.drinkshopapp.Retrofit.IDrinkShopAPI;
import com.ydkim2110.drinkshopapp.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim Yongdae on 2018-11-30
 * email : ydkim2110@gmail.com
 */
public class Common {

    private static final String TAG = "Common";

    public static final String BASE_URL = "http://192.168.0.13/drinkshop/";
    public static final String TOPPING_MENU_ID = "7";

    public static IDrinkShopAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }

    public static User currentUser = null;
    public static Category currentCategory = null;
    public static List<Drink> toppingList = new ArrayList<>();

    public static double toppingPice = 0.0;
    public static List<String> toppingAdded = new ArrayList<>();

    // hold field
    public static int sizeOfCup = -1; // -1 : no choose (error), 0 : M, 1 : L
    public static int sugar = -1; // -1 : no choose (error)
    public static int ice = -1; //

    // database
    public static CartDatabase cartDatabase;
    public static CartRepository cartRepository;

}
