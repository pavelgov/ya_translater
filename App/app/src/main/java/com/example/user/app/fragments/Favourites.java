package com.example.user.app.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.app.R;
import com.example.user.app.activities.ItemTranslateActivity;
import com.example.user.app.adapters.HistoryItemAdapter;
import com.example.user.app.databinding.FragHistoryBinding;
import com.example.user.app.models.HistoryTranslate;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.RealmQuery;

/**
 * Created by User on 12.04.2017.
 */

public class Favourites extends BaseFragment {
    FragHistoryBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_history,container,false);
        binding = DataBindingUtil.bind(view);
        binding.rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        search(null);
        binding.searchHistory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });
        return view;
    }
    private void search(String line){
        List<HistoryTranslate> historyTranslate= new ArrayList<>();
        RealmQuery realmModel = realm
                .where(HistoryTranslate.class)
                .equalTo("favour",true);

        if (line!=null){
            realmModel.beginGroup()
                    .contains("beforeTranslate", line, Case.INSENSITIVE)
                    .or()
                    .contains("translate", line, Case.INSENSITIVE)
                    .endGroup();
        }
        historyTranslate=realmModel.findAll();
        HistoryItemAdapter historyItemAdapter = new HistoryItemAdapter(historyTranslate, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                Intent intent = new Intent(getActivity(), ItemTranslateActivity.class);
                intent.putExtra("primaryKey", tag);
                startActivity(intent);
            }
        });
        binding.rec.setAdapter(historyItemAdapter);


    }

}
