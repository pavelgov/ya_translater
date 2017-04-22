package com.example.user.app.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.user.app.web.WebFunction;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by User on 12.04.2017.
 */

public abstract class BaseFragment extends Fragment {
    public Realm realm;
    public WebFunction webFunction = new WebFunction();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance(); // открытие БД
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    /**
     * преобразует список строк в одну строку формата String
     * @param list
     * @return
     */
    protected String listToString(ArrayList<String> list){
        StringBuilder sb =  new StringBuilder();
        for (String s : list){
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}
