package com.mob.mobapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.mobapp.R;
import com.mob.mobapp.adapters.ChatAdapter;
import com.mob.mobapp.pojos.Message;
import com.mob.mobapp.presenters.ChatPresenter;
import com.mob.mobapp.utils.SystemWorker;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements ScreenView {
    private String uName;
    private String uPhone;
    private Integer cId;
    private TextView textViewChat;
    private ChatPresenter chatPresenter;
    private RecyclerView recyclerViewCenterChat;
    private String cAddress;

    private ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uName = extras.getString("userName");
            uPhone = extras.getString("userPhone");
            cId = extras.getInt("cId");
            cAddress = extras.getString("cAddress");
        }


        setContentView(R.layout.activity_chat);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);

        textViewChat = findViewById(R.id.textViewChat);
        textViewChat.setText(String.format("Чат с мастером: %s", cAddress));

        recyclerViewCenterChat = findViewById(R.id.recyclerViewCenterChat);
        findViewById(R.id.imageButtonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chatPresenter = new ChatPresenter(this);
        chatPresenter.setcId(String.valueOf(cId));
        chatPresenter.setuName(uName);
        chatPresenter.setuPhone(uPhone);
        chatPresenter.loadData();


    }

    @Override
    public void showData(Object object) {
        chatAdapter = new ChatAdapter(this);
        recyclerViewCenterChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        recyclerViewCenterChat.setAdapter(chatAdapter);
        chatAdapter.setMessageArrayList((ArrayList<Message>) object);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
























