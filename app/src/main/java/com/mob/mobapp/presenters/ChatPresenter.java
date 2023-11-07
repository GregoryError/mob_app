package com.mob.mobapp.presenters;

import com.mob.mobapp.api.ApiFactory;
import com.mob.mobapp.pojos.Message;
import com.mob.mobapp.pojos.Order;
import com.mob.mobapp.views.Presentable;
import com.mob.mobapp.views.ScreenView;

import java.util.ArrayList;
import java.util.stream.StreamSupport;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatPresenter implements Presentable {
    private ScreenView screenView;
    private String uName;
    private String uPhone;
    private String cId;

    private boolean needLoad;

    public ChatPresenter(ScreenView screenView) {
        this.screenView = screenView;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    @Override
    public void loadData() {
        if (needLoad) {
            ApiFactory.getInstance().getApiService().getUserMessages(uName, uPhone, cId).enqueue(new Callback<ArrayList<Message>>() {
                @Override
                public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
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
                public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                    screenView.showError(t.getMessage());
                }
            });
        }
    }

    public void start() {
        needLoad = true;
    }

    public void pause() {
        needLoad = false;
    }
}




























