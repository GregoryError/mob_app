package com.mob.mobapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mob.mobapp.api.ApiFactory;
import com.mob.mobapp.utils.SystemWorker;
import com.mob.mobapp.views.MainScreenActivity;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.yandex.mapkit.MapKitFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;


public class MainActivity extends AppCompatActivity {

    public static final String MAP_API_KEY = "829c15f6-14ab-41b7-8b97-7d7dc0e9e64d";
    private TextInputEditText editTextName;
    private TextInputEditText editTextPhone;
    private ProgressBar progressBarAuth;
    private SharedPreferences sharedPref;
    private String fb_token;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Yandex MAP kit
        MapKitFactory.setApiKey(MAP_API_KEY);
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_main);

        askNotificationPermission();

        editTextName = findViewById(R.id.editTextName);
        editTextName.requestFocus();
        editTextPhone = findViewById(R.id.editTextPhone);
        progressBarAuth = findViewById(R.id.progressBarAuth);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        // Set system bar in light
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);


//        sharedPref.edit().remove(getString(R.string.username)).apply();
//        sharedPref.edit().remove("fb_token").apply();

        // check if already authorized
        if (sharedPref.contains(getString(R.string.username))) {
            Intent intent = new Intent(this, MainScreenActivity.class);
            intent.putExtra("userName", sharedPref.getString("userName", ""));
            intent.putExtra("userPhone", sharedPref.getString("userPhone", ""));
            startActivity(intent);
        }

        Button buttonLogin = findViewById(R.id.buttonLogin);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.getText() != null && editTextPhone.getText() != null) {
                    if (fb_token == null)
                        fb_token = "no token";
                    progressBarAuth.setVisibility(ProgressBar.VISIBLE);
                    ApiFactory.getInstance().getApiService().firstAuth(editTextName.getText().toString().trim(),
                            editTextPhone.getText().toString().trim(), fb_token).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            progressBarAuth.setVisibility(ProgressBar.GONE);

                            System.out.println("RESPONSE CODE: " + response.code());

                            switch (response.code()) {
                                // OK (existed user)
                                case 200: {
                                    Toast.makeText(MainActivity.this,
                                            R.string.welcome_200, Toast.LENGTH_LONG).show();
                                    saveAndGoMainScreen();
                                    break;
                                }
                                // created new user OK
                                case 201: {
                                    Toast.makeText(MainActivity.this,
                                            R.string.welcome_201, Toast.LENGTH_LONG).show();
                                    saveAndGoMainScreen();
                                    break;
                                }
                                // ok but without fb-token
                                case 206: {
                                    Toast.makeText(MainActivity.this,
                                            R.string.welcome_206, Toast.LENGTH_LONG).show();
                                    saveAndGoMainScreen();
                                    break;
                                }
                                // Existed user but name-phone mismatch
                                case 400: {
                                    Toast.makeText(MainActivity.this,
                                            R.string.bad_400, Toast.LENGTH_LONG).show();
                                    break;
                                }
                                // Existed user but name-phone mismatch
                                case 403: {
                                    Toast.makeText(MainActivity.this,
                                            R.string.mismatch_403, Toast.LENGTH_LONG).show();
                                    break;
                                }
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        if (!sharedPref.contains("fb_token")) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                fb_token = task.getException().getMessage();
                                System.out.println("Fetching FCM registration token failed: " + task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            fb_token = task.getResult();
                        }
                    });
        }



//
//        // Verbose Logging set to help debug issues, remove before releasing your app.
//        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
//
//        // OneSignal Initialization
//        String ONESIGNAL_APP_ID = "f9892fc0-35d0-405f-8089-d0eb910eaee0";
//        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);
//
//        // requestPermission will show the native Android notification permission prompt.
//        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
//        OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
//            if (r.isSuccess()) {
//                if (r.getData()) {
//                    // `requestPermission` completed successfully and the user has accepted permission
//                } else {
//                    // `requestPermission` completed successfully but the user has rejected permission
//                }
//            } else {
//                // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
//            }
//        }));


    }


    private void saveAndGoMainScreen() {
        editor.putString(getString(R.string.username), editTextName.getText().toString());
        editor.putString(getString(R.string.userphone), editTextPhone.getText().toString());
        editor.putString("fb_token", fb_token);
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
        intent.putExtra("userName", sharedPref.getString("userName", ""));
        intent.putExtra("userPhone", sharedPref.getString("userPhone", ""));
        startActivity(intent);
    }


    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    Snackbar.make(findViewById(R.id.constraintMain), R.string.tnx_text, Snackbar.LENGTH_SHORT);
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Snackbar.make(findViewById(R.id.constraintMain), R.string.notif_disabled_text, Snackbar.LENGTH_SHORT);
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

}