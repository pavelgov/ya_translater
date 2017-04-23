package com.example.user.app.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.app.R;
import com.example.user.app.databinding.ItemHistoryBinding;
import com.example.user.app.models.HistoryTranslate;

import java.util.List;

/**
 * Created by User on 12.04.2017.
 */

/**
 * Адаптер отвечает за преобразование списка объектов "База данных" в визуальные элементы (карточки)
 */
public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {
    List<HistoryTranslate> list;
    View.OnClickListener listener = null;

    public HistoryItemAdapter(List<HistoryTranslate> list, View.OnClickListener listener){
        this.list = list;
        this.listener = listener;
    }

    public void addAll(List<HistoryTranslate> newList){//обновление списка
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();//обновить данные на экране
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
         ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryTranslate item = list.get(position);
        holder.binding.getRoot().setTag(item.getBeforeTranslate());
        holder.binding.beforeTranslate.setText(item.getBeforeTranslate());
        holder.binding.afterTranslate.setText(item.getTranslate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ItemHistoryBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(listener);

        }
    }

}
