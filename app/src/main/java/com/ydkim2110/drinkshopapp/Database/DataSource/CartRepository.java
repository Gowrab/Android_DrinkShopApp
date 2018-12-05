package com.ydkim2110.drinkshopapp.Database.DataSource;

import com.ydkim2110.drinkshopapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Kim Yongdae on 2018-12-02
 * email : ydkim2110@gmail.com
 */
public class CartRepository implements ICartDataSource {

    private ICartDataSource mICartDataSource;

    public CartRepository(ICartDataSource iCartDataSource) {
        this.mICartDataSource = iCartDataSource;
    }

    private static CartRepository instance;

    public static CartRepository getInstance(ICartDataSource iCartDataSource) {
        if (instance == null) {
            instance = new CartRepository(iCartDataSource);
        }
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return mICartDataSource.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return mICartDataSource.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return mICartDataSource.countCartItems();
    }

    @Override
    public float sumPrice() {
        return mICartDataSource.sumPrice();
    }

    @Override
    public void emptyCart() {
        mICartDataSource.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        mICartDataSource.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        mICartDataSource.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        mICartDataSource.deleteCartItem(cart);
    }
}
