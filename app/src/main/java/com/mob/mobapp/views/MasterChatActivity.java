package com.mob.mobapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.mob.mobapp.R;
import com.mob.mobapp.adapters.CenterAdapter;
import com.mob.mobapp.pojos.Center;
import com.mob.mobapp.presenters.CenterViewPresenter;
import com.mob.mobapp.utils.SystemWorker;

import java.util.ArrayList;

public class MasterChatActivity extends AppCompatActivity implements ScreenView{
    private String uName;
    private String uPhone;
    private RecyclerView recyclerViewCenterChat;
    private CenterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uName = extras.getString("userName");
            uPhone = extras.getString("userPhone");
        }

        setContentView(R.layout.activity_master_chat);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);

        findViewById(R.id.imageButtonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerViewCenterChat = findViewById(R.id.recyclerViewCenterChat);
        CenterViewPresenter presenter = new CenterViewPresenter(this);
        presenter.setUserName(uName);
        presenter.setUserPhone(uPhone);
        presenter.loadData();
    }

    @Override
    public void showData(Object object) {
        adapter = new CenterAdapter(this, false);
        recyclerViewCenterChat.setLayoutManager(new GridLayoutManager(this, GridLayoutManager.VERTICAL));
        adapter.setuName(uName);
        adapter.setuPhone(uPhone);
        recyclerViewCenterChat.setAdapter(adapter);
        adapter.setCenterArrayList((ArrayList<Center>) object);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}