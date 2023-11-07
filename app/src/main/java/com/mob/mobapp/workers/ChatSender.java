package com.mob.mobapp.workers;

import com.mob.mobapp.api.ApiFactory;
import com.mob.mobapp.views.Presentable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatSender {

    private String uName;
    private String uPhone;
    private String cId;

    public void setPresenter(Presentable presenter) {
        this.presenter = presenter;
    }

    private Presentable presenter;

    public void setuName(String uName) {
        this.uName = uName;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public void send(String message) {
        ApiFactory.getInstance().getApiService().sendUserMessage(uName, uPhone, cId, message).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                presenter.loadData();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // TODO: retry? shackbar
                System.out.println("Response: error = " + t.getMessage());

            }
        });
    }

}
