package com.mob.mobapp.presenters;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mob.mobapp.R;
import com.mob.mobapp.api.ApiFactory;
import com.mob.mobapp.pojos.InitData;
import com.mob.mobapp.views.Presentable;
import com.mob.mobapp.views.ScreenView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreenPresenter implements Presentable {

    private ScreenView screenView;
    private InitData initDataObj;

    private String userName;

    private String userTel;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public MainScreenPresenter(ScreenView screenView) {
        this.screenView = screenView;
    }

    @Override
    public void loadData() {
        ApiFactory.getInstance().getApiService().getInit(userName, userTel)
                .enqueue(new Callback<InitData>() {
                    @Override
                    public void onResponse(Call<InitData> call, Response<InitData> response) {
                        if (response.isSuccessful()) {
                            initDataObj = new InitData(response.body().getCount(), response.body().getPromos());
                            screenView.showData(initDataObj);
                        }
                    }

                    @Override
                    public void onFailure(Call<InitData> call, Throwable t) {
                        Toast.makeText((AppCompatActivity)screenView, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
