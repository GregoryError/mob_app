package com.mob.mobapp.presenters;

import com.mob.mobapp.api.ApiFactory;
import com.mob.mobapp.pojos.Order;
import com.mob.mobapp.views.Presentable;
import com.mob.mobapp.views.ScreenView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersViewPresenter implements Presentable {
    ScreenView screenView;

    private String userName;
    private String userPhone;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public OrdersViewPresenter(ScreenView screenView) {
        this.screenView = screenView;
    }

    @Override
    public void loadData() {
        ApiFactory.getInstance().getApiService().getOrdersUser(userName, userPhone).enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
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
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                screenView.showError(t.getMessage());
            }
        });
    }
}
