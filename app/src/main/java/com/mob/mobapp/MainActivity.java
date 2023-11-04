package com.mob.mobapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mob.mobapp.api.ApiFactory;
import com.mob.mobapp.utils.SystemWorker;
import com.mob.mobapp.views.MainScreenActivity;
import com.yandex.mapkit.MapKitFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    public static final String MAP_API_KEY = "829c15f6-14ab-41b7-8b97-7d7dc0e9e64d";

    private TextInputEditText editTextName;
    private TextInputEditText editTextPhone;
    private ProgressBar progressBarAuth;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Yandex MAP kit
        MapKitFactory.setApiKey(MAP_API_KEY);
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_main);


        editTextName = findViewById(R.id.editTextName);
        editTextName.requestFocus();
        editTextPhone = findViewById(R.id.editTextPhone);
        progressBarAuth = findViewById(R.id.progressBarAuth);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        // Set system bar in light
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);

        //     sharedPref.edit().remove(getString(R.string.username)).apply();

        // check if already authorized
        if (sharedPref.contains(getString(R.string.username))) {
            Intent intent = new Intent(this, MainScreenActivity.class);
            intent.putExtra("userName", sharedPref.getString("userName", ""));
            intent.putExtra("userPhone", sharedPref.getString("userPhone", ""));
            startActivity(intent);
        }


        Button buttonLogin = findViewById(R.id.buttonLogin);

        // TODO: add FB-token
        buttonLogin.setOnClickListener(v -> {
            if (editTextName.getText() != null && editTextPhone.getText() != null) {
                progressBarAuth.setVisibility(ProgressBar.VISIBLE);
                ApiFactory.getInstance().getApiService().firstAuth(editTextName.getText().toString(),
                        editTextPhone.getText().toString(), "some_token").enqueue(new Callback<Void>() {
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

        });

    }

    private void saveAndGoMainScreen() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.username), editTextName.getText().toString());
        editor.putString(getString(R.string.userphone), editTextPhone.getText().toString());
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
        intent.putExtra("userName", sharedPref.getString("userName", ""));
        intent.putExtra("userPhone", sharedPref.getString("userPhone", ""));
        startActivity(intent);
    }


}