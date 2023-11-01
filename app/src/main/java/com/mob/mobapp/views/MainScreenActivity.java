package com.mob.mobapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mob.mobapp.MainActivity;
import com.mob.mobapp.R;
import com.mob.mobapp.api.ApiFactory;
import com.mob.mobapp.pojos.InitData;
import com.mob.mobapp.presenters.MainScreenPresenter;
import com.mob.mobapp.utils.SystemWorker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreenActivity extends AppCompatActivity implements ScreenView {
    private TextView textViewCount;
    private TextView textViewPromoName;
    private TextView textViewPromoDesc;
    private TextView textViewHelloUser;
    private TextView textViewDiscount;

    private String uName;
    private String uPhone;
    private Toolbar toolbar; // TODO: layout height depends on display; also main cards sizes

    private MainScreenPresenter mainScreenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);

        textViewCount = findViewById(R.id.textViewCount);
        textViewPromoName = findViewById(R.id.textViewPromoName);
        textViewPromoDesc = findViewById(R.id.textViewPromoDesc);
        textViewHelloUser = findViewById(R.id.textViewHelloUser);
        textViewDiscount = findViewById(R.id.textViewDiscount);

        mainScreenPresenter = new MainScreenPresenter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uName = extras.getString("userName");
            uPhone = extras.getString("userPhone");
        }

        mainScreenPresenter.setUserName(uName);
        mainScreenPresenter.setUserTel(uPhone);
        mainScreenPresenter.loadData();
    }

    @Override
    public void showData(Object object) {
        InitData initData = (InitData) object;
        textViewHelloUser.setText(String.format("%s%s", getString(R.string.hi_str), ' ' + uName));
        textViewCount.setText(String.format("%s%s", String.valueOf(initData.getCount()), getString(R.string.bonuses)));
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