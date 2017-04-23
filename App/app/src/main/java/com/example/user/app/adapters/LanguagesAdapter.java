package com.example.user.app.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.app.R;
import com.example.user.app.models.HistoryLanguages;

import java.util.List;

/**
 * Адаптер для Spinner'а с выбором языка перевода
 * Created by User on 21.04.2017.
 */
public class LanguagesAdapter extends ArrayAdapter<HistoryLanguages> {

    private Context context;
    private static final int viewId=R.layout.item_history_lang;

    public LanguagesAdapter(Context context, List<HistoryLanguages> itemList) {
        super(context, viewId,itemList);
        this.context=context;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(viewId, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.lang);
        textView.setText(getItem(position).getLangName());
        return textView;
    }


    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(viewId, parent, false);
        TextView textView = (TextView) row.findViewById(R.id.lang);
        textView.setText(getItem(position).getLangName());

        return textView;
    }


}