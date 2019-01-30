package com.example.amangoyal.funchat;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FunChat extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
