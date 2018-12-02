package com.ydkim2110.drinkshopapp.Database.DataSource;

import com.ydkim2110.drinkshopapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Kim Yongdae on 2018-12-02
 * email : ydkim2110@gmail.com
 */
public interface ICartDataSource {

    Flowable<List<Cart>> getCartItems();
    Flowable<List<Cart>> getCartItemById(int cartItemId);
    int countCartItems();
    void emptyCart();
    void insertToCart(Cart...carts);
    void updateCart(Cart...carts);
    void deleteCartItem(Cart cart);

}
