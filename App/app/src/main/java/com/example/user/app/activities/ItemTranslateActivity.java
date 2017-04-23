package com.example.user.app.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.user.app.R;
import com.example.user.app.databinding.ActivityItemTranslateBinding;
import com.example.user.app.models.HistoryLanguages;
import com.example.user.app.models.HistoryTranslate;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import io.realm.Realm;

public class ItemTranslateActivity extends AppCompatActivity {
    public Realm realm;
    ActivityItemTranslateBinding binding;
    HistoryTranslate historyTranslate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_translate);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_translate);
        realm = Realm.getDefaultInstance(); // открытие БД
        Intent intent = getIntent();
        if (intent != null){
            String primaryKey = intent.getStringExtra("primaryKey");
            if (primaryKey!= null){     //проверяем, что был передан первичный ключ перевода
                 historyTranslate = realm
                        .where(HistoryTranslate.class)
                        .equalTo("beforeTranslate",primaryKey)
                        .findFirst();
                if (historyTranslate!=null){          //если данный перевод найден в ORM, то  выводим на экран полную информацию о нем
                    binding.beforeTranslate.setText(historyTranslate.getBeforeTranslate());
                    binding.afterTranslate.setText(historyTranslate.getTranslate());
                    List<HistoryLanguages> languagesList=realm.where(HistoryLanguages.class).findAll();
                    String from=null;
                    String to=null;
                    if(languagesList.size()>0){     //если база языков заполнена (en-Английский), то находим соответствие с напрвлением текущего перевода
                        String[] base=historyTranslate.getLang().split("-");
                        for(HistoryLanguages historyLanguages:languagesList){
                            if(historyLanguages.getLangCode().equals(base[0]))
                                from=historyLanguages.getLangName();
                            if(historyLanguages.getLangCode().equals(base[1]))
                                to=historyLanguages.getLangName();
                            if (from!=null && to!=null)
                                break;
                        }
                    }
                    if(from!=null && to!=null){
                        binding.language.setText(from+" > "+to);
                    }
                    else{
                        binding.language.setText(getString(R.string.not_found_lang));
                    }
                    try {
                        binding.date.setText(
                                (new DateTime(historyTranslate.getDate()))
                                        .toString(DateTimeFormat.forPattern("HH:mm:ss dd.MM.yyyy"))     //дата создания перевода
                        );
                    }
                    catch (Exception e){
                        binding.date.setText("-");
                    }
                    setTitle(historyTranslate.getBeforeTranslate());
                    if(historyTranslate.isFavour()){
                        binding.favourite.setVisibility(View.GONE);
                    }
                    else {
                        binding.delFavourite.setVisibility(View.GONE);
                    }

                }
            }
        }

        binding.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFav(true);
            }
        });
        binding.delFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFav(false);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Обновляем информацию о данном переводе - избранный он или нет
     * Меняем видимость кнопок
     * @param value - true(избр)/false(нет)
     */
    private void saveFav(boolean value){
        if (historyTranslate!=null){
            realm.beginTransaction();
            historyTranslate.setFavour(value);
            realm.copyToRealmOrUpdate(historyTranslate);
            realm.commitTransaction();
            if(value) {
                binding.favourite.setVisibility(View.GONE);
                binding.delFavourite.setVisibility(View.VISIBLE);
            }
            else {
                binding.favourite.setVisibility(View.VISIBLE);
                binding.delFavourite.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.with_bin, menu);
        return true;
    }


    /**
     * В меню обрабатываем нажатие пункта "корзина" - для удаления данного перевода из ORM и закрываем экран, ибо перевод тю тю
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        if (id == R.id.action_bin){
            if(historyTranslate!=null) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        historyTranslate.deleteFromRealm();
                    }
                });
                Toast.makeText(getApplicationContext(),getString(R.string.info_delete),Toast.LENGTH_LONG).show();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
