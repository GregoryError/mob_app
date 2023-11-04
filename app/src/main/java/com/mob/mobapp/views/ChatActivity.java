package com.mob.mobapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.mob.mobapp.R;
import com.mob.mobapp.utils.SystemWorker;

public class ChatActivity extends AppCompatActivity {
    private String uName;
    private String uPhone;
    private Integer cId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uName = extras.getString("userName");
            uPhone = extras.getString("userPhone");
            cId = extras.getInt("cId");
        }

        setContentView(R.layout.activity_chat);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);

        findViewById(R.id.imageButtonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}