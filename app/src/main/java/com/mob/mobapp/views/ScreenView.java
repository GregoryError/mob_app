package com.mob.mobapp.views;

public interface ScreenView {
    // void showData(JsonObject contentJson);
    void showData(Object object);
    void showError(String message);
}
