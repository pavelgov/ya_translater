package com.example.user.app.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.user.app.R;
import com.example.user.app.activities.ItemTranslateActivity;
import com.example.user.app.adapters.HistoryItemAdapter;
import com.example.user.app.models.HistoryTranslate;
import com.example.user.app.web.WebFunction;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by User on 12.04.2017.
 */

public abstract class BaseSearchFragment extends BaseFragment {

    protected HistoryItemAdapter historyItemAdapter=null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        historyItemAdapter = new HistoryItemAdapter(new ArrayList<HistoryTranslate>(), new View.OnClickListener() {     //инициализируем адаптер записей и передаем обработчик нажатия на заспиь, для перехода на новый экран
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                Intent intent = new Intent(getActivity(), ItemTranslateActivity.class);
                intent.putExtra("primaryKey", tag);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.with_search, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);   //добавляем поле поиска в Toolbar
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {    //при любом изменение поля для поиска - производим поиск записей в ORM по подстроке
                search(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    protected abstract void search(String query);

    /**
     * Перегрузка метода, чтобы было меньше писанины с параметрами
     * @param line
     * @return
     */
    protected RealmResults<HistoryTranslate> selectList(String line){
        return  selectList(line,false);
    }

    /**
     * Функцпия по получению списка искомых результатов по подстркое, с учетом избранных записей или нет
     * @param line
     * @param withFav
     * @return
     */
    protected RealmResults<HistoryTranslate> selectList(String line,boolean withFav){
        final RealmQuery<HistoryTranslate> realmModel = realm
                .where(HistoryTranslate.class);
        if(withFav)
            realmModel.equalTo("favour",true);

        if (line!=null){
            realmModel.beginGroup()
                    .contains("beforeTranslate", line, Case.INSENSITIVE)//поиск без учета регистра по полю..к кириллице бывает привередлива..
                    .or()
                    .contains("translate", line, Case.INSENSITIVE)
                    .endGroup();
        }
        RealmResults<HistoryTranslate> historyTranslate=realmModel.findAll();
        historyTranslate.addChangeListener(new RealmChangeListener<RealmResults<HistoryTranslate>>() {//вешаем обработчик изменений в ORM, на случай, если удалили запись, изменили статус (в избранном или нет)
            @Override
            public void onChange(RealmResults<HistoryTranslate> element) {
                historyItemAdapter.addAll(realmModel.findAll());
            }
        });
        return historyTranslate;

    }

}
