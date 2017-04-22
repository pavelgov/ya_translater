package com.example.user.app.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.user.app.R;
import com.example.user.app.databinding.ActivityItemTranslateBinding;
import com.example.user.app.models.HistoryTranslate;

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
            if (primaryKey!= null){
                 historyTranslate = realm
                        .where(HistoryTranslate.class)
                        .equalTo("beforeTranslate",primaryKey)
                        .findFirst();
                if (historyTranslate!=null){
                    binding.beforeTranslate.setText(historyTranslate.getBeforeTranslate());
                    binding.afterTranslate.setText(historyTranslate.getTranslate());
                    binding.language.setText(historyTranslate.getLang());

                }
            }
        }
        binding.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historyTranslate!=null){
                    realm.beginTransaction();
                    historyTranslate.setFavour(true);
                    realm.copyToRealmOrUpdate(historyTranslate);
                    realm.commitTransaction();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
