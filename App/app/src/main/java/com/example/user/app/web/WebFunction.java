package com.example.user.app.web;

import android.util.Log;

import com.example.user.app.models.DetectLanguage;
import com.example.user.app.models.Languages;
import com.example.user.app.models.Translate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 14.04.2017.
 */

public class WebFunction {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://translate.yandex.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    WebLink service = retrofit.create(WebLink.class);
    public final String KEY = "trnsl.1.1.20170414T143621Z.7e322295b6453c58.acd5bbb78994b197ef1709e241a4dfc7c2c08d03";


    public  void getLanguages( Callback<Languages> callback){
        try{
            Call<Languages> repos = service.getLanguages(KEY, "ru");
            repos.enqueue(callback);
        }catch (Exception e){
            Log.e("Web", "Error");

        }
    }

    public  void getLanguage(String text, Callback<DetectLanguage> callback){
        try{
            Call<DetectLanguage> repos = service.getLanguage(KEY,text);
            repos.enqueue(callback);
        }catch (Exception e){
            Log.e("Web", "Error");

        }
    }
    public  Call<Translate> translate(String text, Callback<Translate> callback){
        try{
            Call<Translate> repos = service.translate(KEY,text,"en-ru","plain");
            repos.enqueue(callback);
            return repos;
        }catch (Exception e){
            Log.e("Web", "Error");

        }
        return null;
    }
}
