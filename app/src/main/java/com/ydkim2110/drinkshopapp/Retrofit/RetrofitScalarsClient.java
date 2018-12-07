package com.ydkim2110.drinkshopapp.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Kim Yongdae on 2018-12-07
 * email : ydkim2110@gmail.com
 */
public class RetrofitScalarsClient {

    private static final String TAG = "RetrofitScalarsClient";

    private static Retrofit retrofit = null;

    public static Retrofit getScalarsClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
