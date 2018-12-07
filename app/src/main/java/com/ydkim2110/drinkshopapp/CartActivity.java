package com.ydkim2110.drinkshopapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.ydkim2110.drinkshopapp.Adapter.CartAdapter;
import com.ydkim2110.drinkshopapp.Adapter.FavoriteAdapter;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Cart;
import com.ydkim2110.drinkshopapp.Database.ModelDB.Favorite;
import com.ydkim2110.drinkshopapp.Retrofit.IDrinkShopAPI;
import com.ydkim2110.drinkshopapp.Utils.Common;
import com.ydkim2110.drinkshopapp.Utils.RecyclerITemTouchHelper;
import com.ydkim2110.drinkshopapp.Utils.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private static final String TAG = "CartActivity";
    private static final int PAYMENT_REQUEST_CODE = 7777;

    private RecyclerView recycler_cart;
    CartAdapter cartAdapter;
    private Button btn_place_order;

    private List<Cart> mCartList = new ArrayList<>();
    private RelativeLayout mRootLayout;

    private IDrinkShopAPI mService;
    private IDrinkShopAPI mServiceScalars;

    private CompositeDisposable mCompositeDisposable;

    private String token, amount, orderAddress, orderComment;
    private HashMap<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Log.d(TAG, "onCreate: started");

        mService = Common.getAPI();
        mServiceScalars = Common.getScalarsAPI();
        mCompositeDisposable = new CompositeDisposable();

        recycler_cart = findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerITemTouchHelper(
                0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        btn_place_order = findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });

        mRootLayout = findViewById(R.id.rootLayout);

        loadCartItems();

        loadToken();
    }

    private void loadToken() {
        Log.d(TAG, "loadToken: called");

        final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder()
                .setContext(CartActivity.this).build();
        waitingDialog.show();
        waitingDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Common.API_TOKEN_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "onFailure: called");
                waitingDialog.dismiss();
                btn_place_order.setEnabled(false);
                Toast.makeText(CartActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "onSuccess: called");
                waitingDialog.dismiss();

                token = responseString;
                btn_place_order.setEnabled(true);

            }
        });
    }

    private void placeOrder() {
        Log.d(TAG, "placeOrder: called");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit Order");


        View submitOrderView = LayoutInflater.from(this).inflate(R.layout.submit_order_layout, null);

        final EditText mComment = submitOrderView.findViewById(R.id.edt_comment);
        final EditText mOtherAddress = submitOrderView.findViewById(R.id.edt_other_address);

        final RadioButton mRadioUserAddress = submitOrderView.findViewById(R.id.rdi_user_address);
        final RadioButton mRadioOtherAddress = submitOrderView.findViewById(R.id.rdi_other_address);

        // event
        mRadioUserAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: Checked");
                    mOtherAddress.setEnabled(false);
                }
            }
        });

        mRadioOtherAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mOtherAddress.setEnabled(true);
                }
            }
        });

        builder.setView(submitOrderView);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                orderComment = mComment.getText().toString();
                if (mRadioUserAddress.isChecked()) {
                    orderAddress = Common.currentUser.getAddress();
                } else if (mRadioOtherAddress.isChecked()) {
                    orderAddress = mOtherAddress.getText().toString();
                } else {
                    orderAddress = "";
                }

                // Payment
                DropInRequest dropInRequest = new DropInRequest().clientToken(token);
                startActivityForResult(dropInRequest.getIntent(CartActivity.this), PAYMENT_REQUEST_CODE);

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                if (Common.cartRepository.sumPrice() > 0) {
                    amount = String.valueOf(Common.cartRepository.sumPrice());
                    params = new HashMap<>();
                    params.put("amount", amount);
                    params.put("nonce", strNonce);

                    sendPayment();
                }
                else {
                    Toast.makeText(this, "Payment amount is 0", Toast.LENGTH_SHORT).show();
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
            }
            else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.e(TAG, "onActivityResult: "+error.getMessage());
            }
        }

    }

    private void sendPayment() {
        Log.d(TAG, "sendPayment: called");

        mServiceScalars.payment(params.get("nonce"), params.get("amount"))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().toString().contains("Successful")) {
                            Toast.makeText(CartActivity.this, "Transaction successful", Toast.LENGTH_SHORT).show();

                            // submit order
                            mCompositeDisposable.add(Common.cartRepository.getCartItems()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Consumer<List<Cart>>() {
                                        @Override
                                        public void accept(List<Cart> carts) throws Exception {
                                            if (!TextUtils.isEmpty(orderAddress)) {
                                                sendOrderToServer(Common.cartRepository.sumPrice(), carts,
                                                        orderComment, orderAddress);
                                            }
                                            else {
                                                Toast.makeText(CartActivity.this, "Order Address Can't null",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }));
                        } else {
                            Toast.makeText(CartActivity.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                        }

                        Log.d(TAG, "onResponse: INFO " + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onFailure: INFO " + t.getMessage());
                    }
                });
    }

    private void sendOrderToServer(float subPrice, List<Cart> carts, String orderComment, String orderAddress) {
        Log.d(TAG, "sendOrderToServer: called");

        if (carts.size() > 0) {
            String orderDetail = new Gson().toJson(carts);
            mService.submitOrder(subPrice, orderDetail, orderComment, orderAddress, Common.currentUser.getPhone())
            .enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(CartActivity.this, "Order submit", Toast.LENGTH_SHORT).show();
                    // Clear cart
                    Common.cartRepository.emptyCart();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, "onFailure: Error" + t.getMessage());
                }
            });
        }
    }

    private void loadCartItems() {
        Log.d(TAG, "loadCartItems: called");
        mCompositeDisposable.add(Common.cartRepository.getCartItems()
            .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<List<Cart>>() {
            @Override
            public void accept(List<Cart> carts) throws Exception {
                displayCartItem(carts);
            }
        }));
    }

    private void displayCartItem(List<Cart> carts) {
        mCartList = carts;
        cartAdapter = new CartAdapter(this, carts);
        recycler_cart.setAdapter(cartAdapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder) {
            String name = mCartList.get(viewHolder.getAdapterPosition()).name;

            final Cart deletedItem = mCartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // delete item from adapter
            cartAdapter.removeItem(deletedIndex);

            // delete item from room database
            Common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(mRootLayout,
                    new StringBuilder(name).append(" removed from Favorites List").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreItem(deletedItem, deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


}
