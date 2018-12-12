package com.ydkim2110.drinkshopapp.Retrofit;

import com.ydkim2110.drinkshopapp.Model.Banner;
import com.ydkim2110.drinkshopapp.Model.Category;
import com.ydkim2110.drinkshopapp.Model.CheckUserResponse;
import com.ydkim2110.drinkshopapp.Model.Drink;
import com.ydkim2110.drinkshopapp.Model.Order;
import com.ydkim2110.drinkshopapp.Model.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Kim Yongdae on 2018-11-30
 * email : ydkim2110@gmail.com
 */
public interface IDrinkShopAPI {

    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExists(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone") String phone,
                               @Field("name") String name,
                               @Field("birthdate") String birthdate,
                               @Field("address") String address);

    @FormUrlEncoded
    @POST("getdrink.php")
    Observable<List<Drink>> getDrink(@Field("menuid") String menuID);

    @FormUrlEncoded
    @POST("getuser.php")
    Call<User> getUserInformation(@Field("phone") String phone);

    @GET("getbanner.php")
    Observable<List<Banner>> getBanners();

    @GET("getmenu.php")
    Observable<List<Category>> getMenu();

    @Multipart
    @POST("upload.php")
    Call<String> uploadFile(@Part MultipartBody.Part phone, @Part MultipartBody.Part file);

    @GET("getalldrinks.php")
    Observable<List<Drink>> getAllDrinks();

    @FormUrlEncoded
    @POST("submitorder.php")
    Call<String> submitOrder(@Field("price") float orderPrice,
                             @Field("orderDetail") String orderDetail,
                             @Field("comment") String comment,
                             @Field("address") String address,
                             @Field("phone") String phone);

    @FormUrlEncoded
    @POST("braintree/checkout.php")
    Call<String> payment(@Field("nonce") String nonce,
                            @Field("amount") String amount);

    @FormUrlEncoded
    @POST("getorder.php")
    Observable<List<Order>> getOrder(@Field("userPhone") String userPhone,
                                    @Field("status") String status);
}
