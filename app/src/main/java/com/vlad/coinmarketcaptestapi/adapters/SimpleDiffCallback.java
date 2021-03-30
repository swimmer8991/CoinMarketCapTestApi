package com.vlad.coinmarketcaptestapi.adapters;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class SimpleDiffCallback<T extends HasId> extends DiffUtil.Callback {
    private List<T> oldList;
    private List<T> newList;

    public SimpleDiffCallback(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldPos, int newPos) {
        T oldItem = oldList.get(oldPos);
        T newItem = newList.get(newPos);
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldPos, int newPos) {
        T oldItem = oldList.get(oldPos);
        T newItem = newList.get(newPos);
        return oldItem.equals(newItem);
    }
}