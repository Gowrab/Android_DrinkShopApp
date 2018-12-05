package com.ydkim2110.drinkshopapp.Database.Local;

import com.ydkim2110.drinkshopapp.Database.DataSource.ICartDataSource;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Kim Yongdae on 2018-12-02
 * email : ydkim2110@gmail.com
 */
public class CartDataSource implements ICartDataSource {

    private CartDAO mCartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        mCartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO) {
        if (instance == null) {
            instance = new CartDataSource(cartDAO);
        }
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return mCartDAO.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return mCartDAO.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return mCartDAO.countCartItems();
    }

    @Override
    public float sumPrice() {
        return mCartDAO.sumPrice();
    }

    @Override
    public void emptyCart() {
        mCartDAO.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        mCartDAO.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        mCartDAO.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        mCartDAO.deleteCartItem(cart);
    }
}
