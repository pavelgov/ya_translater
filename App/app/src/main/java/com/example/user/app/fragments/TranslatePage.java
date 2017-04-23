package com.example.user.app.fragments;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.user.app.MyApp;
import com.example.user.app.R;
import com.example.user.app.adapters.LanguagesAdapter;
import com.example.user.app.databinding.FragTranslatePageBinding;
import com.example.user.app.models.DetectLanguage;
import com.example.user.app.models.HistoryLanguages;
import com.example.user.app.models.HistoryTranslate;
import com.example.user.app.models.Languages;
import com.example.user.app.models.Translate;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 12.04.2017.
 */

public class TranslatePage extends BaseFragment {
    boolean firstSetLanguage = false;
    List<String> listLangCode = new ArrayList<String>();

    int posFrom = -1;
    int posTo = -1;
    Call<Translate> webRequest = null;
    Handler timer = null;

    Runnable timerAction = new Runnable() {
        @Override
        public void run() {
       try{
           getTranslate(fragTranslatePage.trText.getText().toString());
       } catch (Exception e){}
        }
    } ;
    FragTranslatePageBinding fragTranslatePage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_translate_page,container,false);
        setHasOptionsMenu(true);
        fragTranslatePage = DataBindingUtil.bind(view);

        SharedPreferences mySharedPreferences = ((MyApp)getActivity().getApplication()).mySharedPreferences;

        if(!mySharedPreferences.contains(MyApp.APP_AUTO_LOAD) || mySharedPreferences.getBoolean(MyApp.APP_AUTO_LOAD,true)) {
            fragTranslatePage.trText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null) {
                        String line = s.toString();
                        if ((line.length() > 0) && (line.charAt(line.length() - 1) == 32)) {
                            getTranslate(line);
                        }
                        if(line.length() == 0)
                            firstSetLanguage=false;
                        if (timer != null) {
                            timer.removeCallbacks(timerAction);
                        }
                        timer = new Handler();
                        timer.postDelayed(timerAction, 1000);

                    }
                }
            });
        }

        if(realm.where(HistoryLanguages.class).count()==0) {
            webFunction.getLanguages(new Callback<Languages>() {
                @Override
                public void onResponse(Call<Languages> call, Response<Languages> response) {
                    List<HistoryLanguages> list = new ArrayList<>();
                    listLangCode=new ArrayList<String>();
                    for (Map.Entry<String, String> para : response.body().langs.entrySet()) {
                        HistoryLanguages historyLanguages = new HistoryLanguages();
                        historyLanguages.setLangCode(para.getKey());
                        historyLanguages.setLangName(para.getValue());
                        list.add(historyLanguages);
                        listLangCode.add(para.getKey());
                    }
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(list);
                    realm.commitTransaction();

                    initFromTo();
                }

                @Override
                public void onFailure(Call<Languages> call, Throwable t) {
                    try {
                        Toast.makeText(getActivity(), t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e){

                    }
                }
            });
        }
        else{
            initFromTo();
        }
        fragTranslatePage.translateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragTranslatePage.translateTxt.getText().length()>0) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("translate", fragTranslatePage.translateTxt.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), getString(R.string.text_copy), Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    /**
     * Установка базовых настроек для выбора напревления перевода (яэыка)
     */
    private void initFromTo(){
        List<HistoryLanguages> languagesList=realm.where(HistoryLanguages.class).findAllSorted("langName", Sort.ASCENDING);
        listLangCode=new ArrayList<String>();
        for (HistoryLanguages historyLanguages : languagesList) {
            listLangCode.add(historyLanguages.getLangCode());
        }
        LanguagesAdapter languagesAdapter = new LanguagesAdapter(getActivity(), languagesList);//инициализация адаптера с перечнем языок

        fragTranslatePage.from.setAdapter(languagesAdapter);
        fragTranslatePage.to.setAdapter(languagesAdapter);
        fragTranslatePage.from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //устанавливаем обработчик выборанной записи,
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posFrom = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fragTranslatePage.from.setSelection(listLangCode.indexOf("ru"));
        fragTranslatePage.to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posTo = position;
                getTranslate(fragTranslatePage.trText.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fragTranslatePage.to.setSelection(listLangCode.indexOf("en"));
    }


    @Override
    public void onPause() {
        // Удаляем Runnable-объект для прекращения задачи
        timer.removeCallbacks(timerAction);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Handler();
        timer.postDelayed(timerAction, 1000);
    }

    /**
     * Получение перевода из Интернета, или же получение языка на котором написан текст
     * @param line
     */
    private void getTranslate(final String line){
        if (!firstSetLanguage && webFunction.isOnline(getActivity())) {
            if(line!=null && line.length()>0) {
                webFunction.getLanguage(line, new Callback<DetectLanguage>() {
                    @Override
                    public void onResponse(Call<DetectLanguage> call, Response<DetectLanguage> response) {
                        if (response.body() != null && response.body().code==200) {
                            if (!firstSetLanguage) {
                                posFrom = listLangCode.indexOf(response.body().lang);
                                fragTranslatePage.from.setSelection(posFrom);
                                if(posFrom==posTo){
                                    int indexOfRu=listLangCode.indexOf("ru");
                                    if(posTo==indexOfRu)
                                        posTo=listLangCode.indexOf("en");
                                    else
                                        posTo=indexOfRu;

                                    fragTranslatePage.to.setSelection(posTo);
                                }
                                firstSetLanguage = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DetectLanguage> call, Throwable t) {

                    }
                });
            }
        }
        else {
            String lang = null;
            if (posFrom >-1 ){      //определяемся с языком
                lang = listLangCode.get(posFrom) + "-";
            }
            if( posTo > -1 ){
                lang+=listLangCode.get(posTo);
            }
            else {
                lang += "en";
            }
            preload(View.VISIBLE);
            if(line!=null && line.length()>0)
                webRequest = webFunction.translate(line, lang, new Callback<Translate>() {  // запрос в Интернет на перевод текста
                    @Override
                    public void onResponse(Call<Translate> call, Response<Translate> response) {
                        if (response.body() == null) {
                            fragTranslatePage.translateTxt.setText("Не удалось определить язык");
                        } else {

                            String afterTranslate = listToString(response.body().text);
                            final HistoryTranslate historyTranslate = new HistoryTranslate();
                            historyTranslate.setBeforeTranslate(line.trim());
                            historyTranslate.setFavour(false);
                            historyTranslate.setDate((new DateTime()).toString());
                            historyTranslate.setLang(response.body().lang);
                            historyTranslate.setTranslate(afterTranslate);

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(historyTranslate);
                                }
                            });
                            fragTranslatePage.translateTxt.setText(afterTranslate);
                        }
                        webRequest = null;
                        preload(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Translate> call, Throwable t) {
                        Log.e("Web", "Error");
                        Toast.makeText(getActivity(),getString(R.string.error_no_internet),Toast.LENGTH_LONG).show();
                        HistoryTranslate historyTranslate = realm
                                .where(HistoryTranslate.class)
                                .equalTo("beforeTranslate", line)
                                .findFirst();
                        if (historyTranslate != null) {

                            fragTranslatePage.translateTxt.setText(historyTranslate.getTranslate());
                        }
                        webRequest = null;
                        preload(View.GONE);
                    }
                });
        }
    }

    /**
     * отображение и скрытие ProgressBar'а
     * @param state
     */
    private void preload(int state){
        fragTranslatePage.progressBar.setVisibility(state);
        fragTranslatePage.progressText.setVisibility(state);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webRequest != null){
            webRequest.cancel();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.with_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_translate:{
                getTranslate(fragTranslatePage.trText.getText().toString());
                break;
            }
            case R.id.action_clear:{
                fragTranslatePage.trText.setText("");
                fragTranslatePage.translateTxt.setText("");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
