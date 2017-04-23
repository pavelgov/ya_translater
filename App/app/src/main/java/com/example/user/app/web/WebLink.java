package com.example.user.app.web;

import com.example.user.app.models.DetectLanguage;
import com.example.user.app.models.Languages;
import com.example.user.app.models.Translate;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Интерфейс для создания запросов на сайт
 * Created by User on 14.04.2017.
 */
public interface WebLink {
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/detect")
    Call<DetectLanguage> getLanguage(@Field("key") String key, @Field("text") String text); //формирование запроса

    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/getLangs")
    Call<Languages> getLanguages(@Field("key") String key, @Field("ui") String ui); //формирование запроса

    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Call<Translate> translate(@Field("key") String key, @Field("text") String text, @Field("lang") String lang, @Field("format") String format); //формирование запроса

    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Call<Translate> translate(@Field("key") String key, @Field("text") String text, @Field("format") String format); //формирование запроса

}
