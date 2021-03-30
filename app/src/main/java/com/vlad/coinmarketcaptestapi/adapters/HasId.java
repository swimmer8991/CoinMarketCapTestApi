package com.vlad.coinmarketcaptestapi.adapters;

import androidx.annotation.Nullable;

import java.util.List;

public interface HasId {

    long getId();

    static int findInList(HasId entity, @Nullable List<? extends HasId> list) {
        return findInList(entity.getId(), list);
    }

    static int findInList(long id, @Nullable List<? extends HasId> list) {
        if (list == null) return -1;
        for (int i = 0; i < list.size(); i++) {
            HasId listEntity = list.get(i);
            if (listEntity.getId() == id) return i;
        }
        return -1;
    }
}
