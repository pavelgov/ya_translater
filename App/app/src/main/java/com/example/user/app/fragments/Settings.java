package com.example.user.app.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.user.app.MyApp;
import com.example.user.app.R;
import com.example.user.app.databinding.FragSettingsBinding;
import com.example.user.app.databinding.FragTranslatePageBinding;
import com.example.user.app.models.HistoryTranslate;

import io.realm.Realm;

/**
 * Created by User on 12.04.2017.
 */

public class Settings extends BaseFragment {
    FragSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_settings,container,false);
        binding = DataBindingUtil.bind(view);
        if(mySharedPreferences.contains(MyApp.APP_AUTO_LOAD)) {
            binding.switch1.setChecked(mySharedPreferences.getBoolean(MyApp.APP_AUTO_LOAD,true));
        }else
            binding.switch1.setChecked(true);

        binding.delHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());  //диалоговое окно, для убеждения в том, что пользователь в здравом уме и осознает, что очищая историю, удаляет все данные
                dialog.setTitle(getString(R.string.alert));
                dialog.setMessage(getString(R.string.want_del_all));
                dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm
                                        .where(HistoryTranslate.class)
                                        .findAll()
                                        .deleteAllFromRealm();  //удаление из базы данных
                                Toast.makeText(getActivity(),getString(R.string.info_delete_all),Toast.LENGTH_LONG).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();   //закрыть диалог
                    }
                });
                dialog.show();

            }
        });

        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {   //выбор нужен пользователю ли автоперевод
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putBoolean(MyApp.APP_AUTO_LOAD, isChecked);
                editor.apply();
            }
        });

        return view;

    }
}
