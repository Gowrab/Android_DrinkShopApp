package com.ydkim2110.drinkshopapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;
import com.ydkim2110.drinkshopapp.Model.CheckUserResponse;
import com.ydkim2110.drinkshopapp.Model.User;
import com.ydkim2110.drinkshopapp.Retrofit.IDrinkShopAPI;
import com.ydkim2110.drinkshopapp.Utils.Common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1000;

    private Button btn_continue;

    private IDrinkShopAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");
        
        mService = Common.getAPI();

        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginPage(LoginType.PHONE);
            }
        });

        // Check session
        if (AccountKit.getCurrentAccessToken() != null) {
            loginTry();
        }
    }

    private void startLoginPage(LoginType phone) {
        Log.d(TAG, "startLoginPage: called");
        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(phone,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, builder.build());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (result.getError() != null) {
                Toast.makeText(this, ""+result.getError().getErrorType().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            else if (result.wasCancelled()) {
                Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
            }
            else {
                if (result.getAccessToken() != null) {
                    loginTry();
                }
            }
        }
    }

    private void loginTry() {
        final AlertDialog alertDialog = new SpotsDialog.Builder()
                .setContext(MainActivity.this).build();
        alertDialog.setMessage("잠시만 기다려 주세요...");
        alertDialog.show();

        // Auto Login
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                mService.checkUserExists(account.getPhoneNumber().toString())
                        .enqueue(new Callback<CheckUserResponse>() {
                            @Override
                            public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                CheckUserResponse userResponse = response.body();
                                Log.d(TAG, "onResponse: called");
                                if (userResponse.isExists()) {
                                    Log.d(TAG, "onResponse: if");
                                    // Fetch Information
                                    mService.getUserInformation(account.getPhoneNumber().toString())
                                            .enqueue(new Callback<User>() {
                                                @Override
                                                public void onResponse(Call<User> call, Response<User> response) {
                                                    Common.currentUser = response.body();
                                                    // If user already exists, just start new activity
                                                    alertDialog.dismiss();
                                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                                    finish(); // close mainactivity
                                                }

                                                @Override
                                                public void onFailure(Call<User> call, Throwable t) {
                                                    Toast.makeText(MainActivity.this, ""+t.getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                                else {
                                    Log.d(TAG, "onResponse: else");
                                    // else, need register
                                    alertDialog.dismiss();

                                    showRegisterDialog(account.getPhoneNumber().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure: called: " + t.getMessage());
                                alertDialog.dismiss();
                            }
                        });
            }

            @Override
            public void onError(AccountKitError accountKitError) {
                Log.d(TAG, "onError: called: " + accountKitError.getErrorType().getMessage());
            }
        });
    }

    private void showRegisterDialog(final String phone) {
        Log.d(TAG, "showRegisterDialog: called");
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("회원가입");

        LayoutInflater inflater = this.getLayoutInflater();
        View register_layout = inflater.inflate(R.layout.register_layout, null);

        final MaterialEditText edt_name = register_layout.findViewById(R.id.edt_name);
        final MaterialEditText edt_birthdate = register_layout.findViewById(R.id.edt_birthdate);
        final MaterialEditText edt_address = register_layout.findViewById(R.id.edt_address);

        Button btn_register = register_layout.findViewById(R.id.btn_register);

        edt_birthdate.addTextChangedListener(new PatternedTextWatcher("####-##-##"));

        builder.setView(register_layout);
        final AlertDialog dialog = builder.create();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                if (TextUtils.isEmpty(edt_name.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_birthdate.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your birthdate",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_address.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your address",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                final AlertDialog waitingDialog = new SpotsDialog.Builder()
                        .setContext(MainActivity.this).build();
                waitingDialog.setMessage("잠시만 기다려 주세요...");
                waitingDialog.show();

                mService.registerNewUser(phone,
                        edt_name.getText().toString(),
                        edt_birthdate.getText().toString(),
                        edt_address.getText().toString())
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                waitingDialog.dismiss();
                                User user = response.body();
                                if (TextUtils.isEmpty(user.getError_msg())) {
                                    Toast.makeText(MainActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                    Common.currentUser = response.body();
                                    // start new activity
                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                waitingDialog.dismiss();
                            }
                        });
            }

        });
        dialog.show();
    }

    private void printKeyHash() {
        try {
            if(Build.VERSION.SDK_INT >= 28) {
                Log.d(TAG, "printKeyHash: if in");
                @SuppressLint("WrongConstant")
                final PackageInfo packageInfo = getPackageManager().getPackageInfo(
                        getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                final Signature[] signatures = packageInfo.signingInfo.getApkContentsSigners();
                final MessageDigest md = MessageDigest.getInstance("SHA");
                for (Signature signature : signatures) {
                    md.update(signature.toByteArray());
                    final String signatureBase64 = new String(Base64.encode(md.digest(), Base64.DEFAULT));
                    Log.d("KeyHash: ", signatureBase64);
                }
            } else {
                PackageInfo info = getPackageManager().getPackageInfo(
                        getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            }
            Log.d(TAG, "printKeyHash: if out");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
