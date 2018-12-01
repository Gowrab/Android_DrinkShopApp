package com.ydkim2110.drinkshopapp.Utils;

import com.ydkim2110.drinkshopapp.Model.User;
import com.ydkim2110.drinkshopapp.Retrofit.IDrinkShopAPI;
import com.ydkim2110.drinkshopapp.Retrofit.RetrofitClient;

/**
 * Created by Kim Yongdae on 2018-11-30
 * email : ydkim2110@gmail.com
 */
public class Common {

    private static final String TAG = "Common";

    private static final String BASE_URL = "http://192.168.0.13/drinkshop/";

    public static IDrinkShopAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }

    public static User currentUser = null;

}
