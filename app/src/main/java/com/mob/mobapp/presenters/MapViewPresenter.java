package com.mob.mobapp.presenters;

import androidx.appcompat.app.AppCompatActivity;

import com.mob.mobapp.R;
import com.mob.mobapp.api.ApiFactory;
import com.mob.mobapp.pojos.Center;
import com.mob.mobapp.views.Presentable;
import com.mob.mobapp.views.ScreenView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewPresenter implements Presentable {
    private String userName;
    private String userPhone;
    private ScreenView screenView;


    @Override
    public void loadData() {
        ApiFactory.getInstance().getApiService().getCentersUser(userName, userPhone).enqueue(new Callback<ArrayList<Center>>() {
            @Override
            public void onResponse(Call<ArrayList<Center>> call, Response<ArrayList<Center>> response) {
                System.out.println("CENTER: " + response.code());
                switch (response.code()) {
                    case 200: {
                        screenView.showData(response.body());
                        break;
                    }
                    case 204: {
                        screenView.showError("204: Нет данных.");
                        break;
                    }
                    case 403: {
                        screenView.showError("403: Неверная авторизация.");
                        break;
                    }
                    default:
                        break;
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Center>> call, Throwable t) {
                screenView.showError(t.getMessage());
            }
        });
    }

    public MapViewPresenter(ScreenView screenView) {
        this.screenView = screenView;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
