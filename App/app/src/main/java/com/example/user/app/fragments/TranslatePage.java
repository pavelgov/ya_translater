package com.example.user.app.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.app.R;
import com.example.user.app.databinding.FragTranslatePageBinding;
import com.example.user.app.models.HistoryTranslate;
import com.example.user.app.models.Translate;

import org.joda.time.DateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 12.04.2017.
 */

public class TranslatePage extends BaseFragment {
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
        fragTranslatePage = DataBindingUtil.bind(view);


        /*fragTranslatePage.translateGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        fragTranslatePage.trText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if (s != null){
                   String line = s.toString();
                   if ((line.length() > 0) && (line.charAt(line.length()-1)==32)){
                       getTranslate(line);
                   }
                   if (timer!=null){
                       timer.removeCallbacks(timerAction);
                   }
                   timer = new Handler();
                   timer.postDelayed(timerAction, 1000);

               }
            }
        });
        return view;
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

    private void getTranslate(final String line){
       webRequest = webFunction.translate(line, new Callback<Translate>() {
            @Override
            public void onResponse(Call<Translate> call, Response<Translate> response) {
                if (response.body() == null){
                    fragTranslatePage.translateTxt.setText("Не удалось определить язык");
                } else{
                    String afterTranslate = listToString(response.body().text);
                    HistoryTranslate historyTranslate = new HistoryTranslate();
                    historyTranslate.setBeforeTranslate(line);
                    historyTranslate.setFavour(false);
                    historyTranslate.setDate((new DateTime()).toString());
                    historyTranslate.setLang(response.body().lang);
                    historyTranslate.setTranslate(afterTranslate);
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(historyTranslate);
                    realm.commitTransaction();
                    fragTranslatePage.translateTxt.setText(afterTranslate);

                }
                webRequest = null;
            }

            @Override
            public void onFailure(Call<Translate> call, Throwable t) {
                Log.e("Web", "Error");
                HistoryTranslate historyTranslate = realm
                        .where(HistoryTranslate.class)
                        .equalTo("beforeTranslate",line)
                        .findFirst();
                if (historyTranslate!=null){

                    fragTranslatePage.translateTxt.setText(historyTranslate.getTranslate());
                }
                webRequest = null;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webRequest != null){
            webRequest.cancel();
        }
    }
}
