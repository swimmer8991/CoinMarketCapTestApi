package com.vlad.coinmarketcaptestapi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Lists {

    public static <I, O> List<O> map(Collection<I> inputCollection, Function<I, O> mapper) {
        if (inputCollection == null) return null;

        List<O> mappedList = new ArrayList<>(inputCollection.size());
        for (I inputEntity : inputCollection) {
            mappedList.add(mapper.apply(inputEntity));
        }
        return mappedList;
    }

    public static <T> List<T> filter(Collection<T> inputCollection, Predicate<T> filter) {
        List<T> filteredList = new ArrayList<>();
        for (T entity : inputCollection) {
            if (filter.test(entity)) filteredList.add(entity);
        }
        return filteredList;
    }

}
