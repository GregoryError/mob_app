package com.mob.mobapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;
import com.mob.mobapp.R;
import com.mob.mobapp.adapters.OrdersAdapter;
import com.mob.mobapp.pojos.Order;
import com.mob.mobapp.presenters.OrdersViewPresenter;
import com.mob.mobapp.utils.SystemWorker;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity implements ScreenView {
    private String uName;
    private String uPhone;

    private RecyclerView recyclerViewOrders;
    private ImageButton imageButtonBack;
    private OrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uName = extras.getString("userName");
            uPhone = extras.getString("userPhone");
        }

        setContentView(R.layout.activity_orders);

        // set system bar color
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);


        imageButtonBack = findViewById(R.id.imageButtonBack);
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        OrdersViewPresenter presenter = new OrdersViewPresenter(this);
        presenter.setUserName(uName);
        presenter.setUserPhone(uPhone);
        presenter.loadData();
    }

    @Override
    public void showData(Object object) {
        recyclerViewOrders.setLayoutManager(new GridLayoutManager(this, GridLayoutManager.VERTICAL));
        adapter = new OrdersAdapter(this);
        recyclerViewOrders.setAdapter(adapter);
        adapter.setOrderArrayList((ArrayList<Order>) object);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(findViewById(R.id.constraintOrders), message,
                Snackbar.LENGTH_LONG).show();
    }
}