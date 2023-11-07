package com.mob.mobapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.mobapp.R;
import com.mob.mobapp.adapters.ChatAdapter;
import com.mob.mobapp.pojos.Message;
import com.mob.mobapp.presenters.ChatPresenter;
import com.mob.mobapp.utils.SystemWorker;
import com.mob.mobapp.workers.ChatSender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity implements ScreenView {
    private String uName;
    private String uPhone;
    private Integer cId;
    private TextView textViewChat;
    private ChatPresenter chatPresenter;
    private RecyclerView recyclerViewCenterChat;
    private String cAddress;
    private ChatAdapter chatAdapter = null;
    private ImageButton buttonChatSend;
    private AppCompatMultiAutoCompleteTextView editChatMessage;

    private ArrayList<Message> t_messages = null;

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
        buttonChatSend = findViewById(R.id.buttonChatSend);
        editChatMessage = findViewById(R.id.editChatMessage);

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
        chatPresenter.start();
        chatPresenter.loadData();


        ChatSender chatSender = new ChatSender();
        chatSender.setuName(uName);
        chatSender.setuPhone(uPhone);
        chatSender.setcId(String.valueOf(cId));
        chatSender.setPresenter(chatPresenter);

        buttonChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editChatMessage.getText().length() > 0) {
                    chatSender.send(editChatMessage.getText().toString().trim());
                    editChatMessage.getText().clear();
                }
            }
        });


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int pos = 0;
                if (((LinearLayoutManager) recyclerViewCenterChat.getLayoutManager()) != null)
                    pos = ((LinearLayoutManager) recyclerViewCenterChat.getLayoutManager())
                            .findLastVisibleItemPosition();

                if (chatAdapter != null && pos == chatAdapter.getItemCount() - 1) {
                    chatPresenter.loadData();
                }

            }
        }, 300, 3000);
    }

    @Override
    public void showData(Object object) {
        // TODO: sort by time

        if (t_messages == null || ((ArrayList<Message>) object).size() > t_messages.size()) {
            t_messages = (ArrayList<Message>) object;
            chatAdapter = new ChatAdapter(this);
            recyclerViewCenterChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
            recyclerViewCenterChat.setAdapter(chatAdapter);
            Collections.reverse(t_messages);
            chatAdapter.setMessageArrayList(t_messages);
            recyclerViewCenterChat.scrollToPosition(0);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
























