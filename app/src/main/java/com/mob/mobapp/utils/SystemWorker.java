package com.mob.mobapp.utils;

import android.view.View;
import android.view.Window;

public class SystemWorker {

    private static SystemWorker instance;

    public static synchronized SystemWorker getInstance() {
        if (instance == null) {
            instance = new SystemWorker();
        }
        return instance;
    }


    // Device system bar
    public void changeStatusBarContrastStyle(Window window, Boolean lightIcons) {
        View decorView = window.getDecorView();
        if (lightIcons) {
            // Draw light icons on a dark background color
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            // Draw dark icons on a light background color
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
