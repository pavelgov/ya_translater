package com.example.user.app.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by User on 14.04.2017.
 */

@RealmClass
public class AcceptLanguages extends RealmObject {

    @PrimaryKey
    private String langToLang;

    public String getLangToLang() {
        return langToLang;
    }

    public void setLangToLang(String langToLang) {
        this.langToLang = langToLang;
    }
}
