package com.vlad.coinmarketcaptestapi;

import com.vlad.coinmarketcaptestapi.providers.CoinMarketCapProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.Getter;

@Getter
@Singleton
public class AppProviders {

    private CoinMarketCapProvider coinMarketCapProvider;

    @Inject
    AppProviders(CoinMarketCapProvider coinMarketCapProvider) {
        this.coinMarketCapProvider = coinMarketCapProvider;
    }
}
