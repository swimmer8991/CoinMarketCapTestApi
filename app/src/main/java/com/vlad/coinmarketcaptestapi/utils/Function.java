package com.vlad.coinmarketcaptestapi.utils;

public interface Function<I, O> {
    O apply(I input);
}
