package com.example.user.app.models;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by User on 14.04.2017.
 */

@RealmClass
public class HistoryLanguages extends RealmObject {

    @PrimaryKey
    private String langCode;
    private String langName;

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }
}
