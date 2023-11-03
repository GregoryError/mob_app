package com.mob.mobapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;
import com.mob.mobapp.R;
import com.mob.mobapp.adapters.CenterAdapter;
import com.mob.mobapp.pojos.Center;
import com.mob.mobapp.presenters.CenterViewPresenter;
import com.mob.mobapp.utils.SystemWorker;

import java.util.ArrayList;

public class CentersActivity extends AppCompatActivity implements ScreenView {

    private ImageButton imageButtonBack;
    private RecyclerView recyclerViewCenter;

    private String uName;
    private String uPhone;

    private CenterAdapter centerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uName = extras.getString("userName");
            uPhone = extras.getString("userPhone");
        }

        setContentView(R.layout.activity_centers);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);

        imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerViewCenter = findViewById(R.id.recyclerViewCenter);



        CenterViewPresenter presenter = new CenterViewPresenter(this);
        presenter.setUserName(uName);
        presenter.setUserPhone(uPhone);
        presenter.loadData();

    }

    @Override
    public void showData(Object object) {
        recyclerViewCenter.setLayoutManager(new GridLayoutManager(this, GridLayoutManager.VERTICAL));
        centerAdapter = new CenterAdapter(this);
        recyclerViewCenter.setAdapter(centerAdapter);
        centerAdapter.setCenterArrayList((ArrayList<Center>) object);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(findViewById(R.id.constraintCenters), message, Snackbar.LENGTH_LONG).show();
    }
}










