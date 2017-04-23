package com.example.user.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by User on 15.04.2017.
 */
public class MyApp extends Application {
    private final String APP_PREFERENCES = "myPref";
    public static final String APP_AUTO_LOAD = "autoload";
    public SharedPreferences mySharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);   //Инициализация ORM

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(2)   //версия ORM
                .deleteRealmIfMigrationNeeded() //при изменении структуры ORM все схемы очищаются от старых данных
                .build();

        Realm.setDefaultConfiguration(config);
        mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

}

