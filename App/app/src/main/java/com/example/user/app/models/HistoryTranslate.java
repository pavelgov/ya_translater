package com.example.user.app.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by User on 15.04.2017.
 */

@RealmClass
public class HistoryTranslate extends RealmObject {
    private boolean favour;
    private String translate;
    private String lang;
    private String date;
    @PrimaryKey
    private String beforeTranslate;

    public boolean isFavour() {
        return favour;
    }

    public void setFavour(boolean favour) {
        this.favour = favour;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBeforeTranslate() {
        return beforeTranslate;
    }

    public void setBeforeTranslate(String beforeTranslate) {
        this.beforeTranslate = beforeTranslate;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
