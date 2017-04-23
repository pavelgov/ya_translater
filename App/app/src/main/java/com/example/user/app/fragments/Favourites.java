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
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by User on 12.04.2017.
 */

public class Favourites extends BaseSearchFragment {
    private FragHistoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_history,container,false);
        binding = DataBindingUtil.bind(view);
        binding.rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        search(null);
        return view;
    }

    /**
     * Поиск записей по параматрам
     * @param line
     */
    @Override
    protected void search(final String line){
        historyItemAdapter.addAll(selectList(line,true));
        binding.rec.setAdapter(historyItemAdapter);
    }
}
