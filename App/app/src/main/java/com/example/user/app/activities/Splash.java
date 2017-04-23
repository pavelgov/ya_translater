package com.example.user.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.user.app.R;
import com.example.user.app.models.HistoryLanguages;
import com.example.user.app.models.Languages;
import com.example.user.app.web.WebFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Splash extends AppCompatActivity {

    WebFunction webFunction=new WebFunction();
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        realm=Realm.getDefaultInstance();
        List<HistoryLanguages> languagesList=realm.where(HistoryLanguages.class).findAll();
        if(webFunction.isOnline(this) && languagesList.size()==0) {//Если Интернет доступн и база языков пуста, то
            webFunction.getLanguages(new Callback<Languages>() {        //загружаем из Интернета доступные языки для перевода
                @Override
                public void onResponse(Call<Languages> call, Response<Languages> response) {
                    if (response.body() != null) {
                        try {
                            List<HistoryLanguages> list = new ArrayList<>();
                            for (Map.Entry<String, String> para : response.body().langs.entrySet()) {   //Так как Realm (ORM) не восприимчива к классу HashMap,
                                                                                                        // то приходится преобразовывать данные в более адекватную для неё форму.
                                HistoryLanguages historyLanguages = new HistoryLanguages();
                                historyLanguages.setLangCode(para.getKey());
                                historyLanguages.setLangName(para.getValue());
                                list.add(historyLanguages);
                            }
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(list);    //сохраняем записи в ORM
                            realm.commitTransaction();
                        } catch (Exception e) {
                            Log.e(this.getClass().getSimpleName(), "Error with parse langs");
                        }
                    } else {
                        Toast.makeText(Splash.this, getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
                    }
                    finish();
                }

                @Override
                public void onFailure(Call<Languages> call, Throwable t) {
                    try {
                        Toast.makeText(Splash.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e){

                    }
                    finish();
                }
            });
        }
        else {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        startActivity(new Intent(this,MainActivity.class));
    }
}
