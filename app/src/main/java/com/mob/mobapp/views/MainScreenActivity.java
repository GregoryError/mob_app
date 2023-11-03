package com.mob.mobapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mob.mobapp.R;
import com.mob.mobapp.pojos.InitData;
import com.mob.mobapp.presenters.MainScreenPresenter;
import com.mob.mobapp.utils.Permissions;
import com.mob.mobapp.utils.SystemWorker;

import org.w3c.dom.ls.LSParser;

public class MainScreenActivity extends AppCompatActivity implements ScreenView {

    private Activity self = this;
    private TextView textViewCount;
    private TextView textViewPromoName;
    private TextView textViewPromoDesc;
    private TextView textViewHelloUser;
    private TextView textViewDiscount;

    private Button button_0;
    private Button button_1;
    private Button button_2;
    private Button button_3;
    private Button button_4;
    private Button button_5;

    private String uName;
    private String uPhone;
    private Toolbar toolbar; // TODO: layout height depends on display; also main cards sizes

    private MainScreenPresenter mainScreenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uName = extras.getString("userName");
            uPhone = extras.getString("userPhone");
        }


        setContentView(R.layout.activity_main_screen);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);

        textViewCount = findViewById(R.id.textViewCount);
        textViewPromoName = findViewById(R.id.textViewPromoName);
        textViewPromoDesc = findViewById(R.id.textViewPromoDesc);
        textViewHelloUser = findViewById(R.id.textViewHelloUser);
        textViewDiscount = findViewById(R.id.textViewDiscount);

        button_0 = findViewById(R.id.button_0);
        button_1 = findViewById(R.id.button_1);
        button_2 = findViewById(R.id.button_2);
        button_3 = findViewById(R.id.button_3);
        button_4 = findViewById(R.id.button_4);
        button_5 = findViewById(R.id.button_5);

        button_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapViewActivity.class);
                intent.putExtra("userName", uName);
                intent.putExtra("userPhone", uPhone);
                startActivity(intent);
            }
        });

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+78129562358"));// Initiates the Intent
                startActivity(intent);
            }
        });

        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CentersActivity.class);
                intent.putExtra("userName", uName);
                intent.putExtra("userPhone", uPhone);
                startActivity(intent);
            }
        });


        // Load main screen data
        mainScreenPresenter = new MainScreenPresenter(this);
        mainScreenPresenter.setUserName(uName);
        mainScreenPresenter.setUserTel(uPhone);
        mainScreenPresenter.loadData();
    }

    @Override
    public void showData(Object object) {
        InitData initData = (InitData) object;
        textViewHelloUser.setText(String.format("%s%s", getString(R.string.hi_str), ' ' + uName));
        textViewCount.setText(String.format("%s%s", String.valueOf(initData.getCount()),
                getString(R.string.bonuses)));
        String discountStr = getString(R.string.discount_text) + ' ' +
                (Integer) initData.getCount() / 100 + "%";
        textViewDiscount.setText(discountStr);

        // TODO: promo carousel

        textViewPromoName.setText(initData.getPromos().get(0).getName());
        textViewPromoDesc.setText(initData.getPromos().get(0).getDescription());
        System.out.println("Promos: ");
        for (int i = 0; i < initData.getPromos().size(); ++i) {
            System.out.println("Promo: " + initData.getPromos().get(i).getName());
        }

    }

    @Override
    public void showError(String message) {

    }



}