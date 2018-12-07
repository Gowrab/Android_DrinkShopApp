package com.ydkim2110.drinkshopapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;
import com.ydkim2110.drinkshopapp.Adapter.CategoryAdapter;
import com.ydkim2110.drinkshopapp.Database.DataSource.CartRepository;
import com.ydkim2110.drinkshopapp.Database.DataSource.FavoriteRepository;
import com.ydkim2110.drinkshopapp.Database.Local.CartDataSource;
import com.ydkim2110.drinkshopapp.Database.Local.FavoriteDataSource;
import com.ydkim2110.drinkshopapp.Database.Local.YDKIMRoomDatabase;
import com.ydkim2110.drinkshopapp.Model.Banner;
import com.ydkim2110.drinkshopapp.Model.Category;
import com.ydkim2110.drinkshopapp.Model.CheckUserResponse;
import com.ydkim2110.drinkshopapp.Model.Drink;
import com.ydkim2110.drinkshopapp.Model.User;
import com.ydkim2110.drinkshopapp.Retrofit.IDrinkShopAPI;
import com.ydkim2110.drinkshopapp.Utils.Common;
import com.ydkim2110.drinkshopapp.Utils.ProgressRequestBody;
import com.ydkim2110.drinkshopapp.Utils.UploadCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UploadCallBack {

    private static final String TAG = "HomeActivity";
    private static final int PICK_FILE_REQUEST = 1222;

    private TextView txt_name, txt_phone;

    private SliderLayout mSliderLayout;

    private IDrinkShopAPI mService;

    private RecyclerView lst_menu;

    private NotificationBadge badge;
    private ImageView cart_icon;

    private CircleImageView img_avatar;
    private Uri selectedFileUri;

    // Rxjava
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mService = Common.getAPI();

        lst_menu = findViewById(R.id.lst_menu);
        lst_menu.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        lst_menu.setHasFixedSize(true);

        mSliderLayout = findViewById(R.id.slider);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txt_name = headerView.findViewById(R.id.txt_name);
        txt_phone = headerView.findViewById(R.id.txt_phone);
        img_avatar = headerView.findViewById(R.id.img_avatar);

        // Event
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        if (Common.currentUser != null) {

            // set info
            txt_name.setText(Common.currentUser.getName());
            txt_phone.setText(Common.currentUser.getPhone());

            // set avatar
            if (!TextUtils.isEmpty(Common.currentUser.getAvatarUrl())) {
                Picasso.with(this)
                        .load(new StringBuilder(Common.BASE_URL)
                                .append("user_avatar/")
                                .append(Common.currentUser.getAvatarUrl()).toString())
                        .into(img_avatar);
            }

        }

        // get banner
        getBannerImage();

        // get menu
        getMenu();
        
        // save newest topping list
        getToppingList();

        // init database
        initDB();

        checkSessionLogin(); // if user already logged, just login again (Session still live)
    }

    private void checkSessionLogin() {
        Log.d(TAG, "checkSessionLogin: called");

        if (AccountKit.getCurrentAccessToken() != null) {
            final AlertDialog dialog = new SpotsDialog.Builder().setContext(HomeActivity.this).build();
            dialog.setMessage("잠시만 기다려 주세요...");
            dialog.show();

            // check exists user on Server (MYSQL)
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    mService.checkUserExists(account.getPhoneNumber().toString())
                            .enqueue(new Callback<CheckUserResponse>() {
                                @Override
                                public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                    CheckUserResponse userResponse = response.body();

                                    if (userResponse.isExists()) {
                                        // Request Information of current user
                                        mService.getUserInformation(account.getPhoneNumber().toString())
                                                .enqueue(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {
                                                        Common.currentUser = response.body();
                                                        if (Common.currentUser != null) {
                                                            dialog.dismiss();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {
                                                        dialog.dismiss();
                                                        Log.e(TAG, "onFailure: "+t.getMessage());
                                                    }
                                                });
                                    }
                                    else {
                                        // if user not exists on Database, just make login
                                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                                    Log.e(TAG, "onFailure: "+t.getMessage());
                                }
                            });
                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    Log.e(TAG, "onFailure: "+accountKitError.getErrorType());
                }
            });
        }
    }

    private void chooseImage() {
        Log.d(TAG, "chooseImage: called");
        startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(),
                "Select a file"),
                PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data != null) {
                    selectedFileUri = data.getData();
                    if (selectedFileUri != null && !selectedFileUri.getPath().isEmpty()) {
                        img_avatar.setImageURI(selectedFileUri);
                        uploadFile();
                    }
                    else {
                        Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void uploadFile() {
        Log.d(TAG, "uploadFile: called");
        if (selectedFileUri != null) {
            File file = FileUtils.getFile(this, selectedFileUri);

            String fileName = new StringBuilder(Common.currentUser.getPhone())
                    .append(FileUtils.getExtension(file.toString()))
                    .toString();

            ProgressRequestBody requestFile = new ProgressRequestBody(file, this);

            Log.d(TAG, "uploadFile: fileName: " + fileName);
            Log.d(TAG, "uploadFile: requestFile: " + requestFile);

            final MultipartBody.Part body = MultipartBody.Part
                    .createFormData("uploaded_file", fileName, requestFile);

            final MultipartBody.Part userPhone = MultipartBody.Part
                    .createFormData("phone", Common.currentUser.getPhone());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mService.uploadFile(userPhone, body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(HomeActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).start();
        }
    }

    private void initDB() {
        Log.d(TAG, "initDB: called");

        Common.ydkimRoomDatabase = YDKIMRoomDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(
                CartDataSource.getInstance(Common.ydkimRoomDatabase.cartDAO()));
        Common.favoriteRepository = FavoriteRepository.getInstance(
                FavoriteDataSource.getInstance(Common.ydkimRoomDatabase.favoriteDAO()));
    }

    private void getToppingList() {
        Log.d(TAG, "getToppingList: called");
        mCompositeDisposable.add(mService.getDrink(Common.TOPPING_MENU_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        Common.toppingList = drinks;
                    }
        }));
    }

    private void getMenu() {
        Log.d(TAG, "getMenu: called");
        mCompositeDisposable.add(mService.getMune()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        displayMenu(categories);
                    }
        }));
    }

    private void displayMenu(List<Category> categories) {
        Log.d(TAG, "displayMenu: called");
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        lst_menu.setAdapter(adapter);
    }


    private void getBannerImage() {
        Log.d(TAG, "getBannerImage: called");
        mCompositeDisposable.add(mService.getBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {
                        displayImage(banners);
                    }
        }));
    }


    private void displayImage(List<Banner> banners) {
        Log.d(TAG, "displayImage: called");
        HashMap<String, String> bannerMap = new HashMap<>();
        for (Banner item : banners) {
            bannerMap.put(item.getName(), item.getLink());
        }

        for (String name : bannerMap.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            mSliderLayout.addSlider(textSliderView);
        }
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.dispose();
        super.onDestroy();
    }

    // exist application when click back button
    boolean isBackButtonClicked = false;

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isBackButtonClicked) {
                super.onBackPressed();
                return;
            }
            this.isBackButtonClicked = true;
            Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = view.findViewById(R.id.badge);
        cart_icon = view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        updateCartCount();
        return true;
    }

    private void updateCartCount() {
        if (badge == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRepository.countCartItems() == 0) {
                    badge.setVisibility(View.INVISIBLE);
                } else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItems()));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart_menu) {
            return true;
        }
        else if (id == R.id.search_menu) {
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sign_out) {
           // create confirm dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit Application");
            builder.setMessage("Do you want to exit this application ?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    AccountKit.logOut();

                    // clear all activity
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                    dialogInterface.dismiss();
                }
            });

            builder.setNegativeButton("CANCEL", null);
            builder.show();
        }
        else if (id == R.id.nav_favorite) {
            startActivity(new Intent(HomeActivity.this, FavoriteListActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        isBackButtonClicked = false;
        super.onResume();
        updateCartCount();
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }
}
