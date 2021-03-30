package com.vlad.coinmarketcaptestapi.providers;

import com.vlad.coinmarketcaptestapi.api.ApiResponse;
import com.vlad.coinmarketcaptestapi.api.CoinMarketCapApi;
import com.vlad.coinmarketcaptestapi.model.Coin;

import java.util.List;

import io.reactivex.Single;

public class CoinMarketCapProvider {

    CoinMarketCapApi api;

    public CoinMarketCapProvider(CoinMarketCapApi api) {
        this.api = api;
    }

    public Single<ApiResponse<List<Coin>>> loadCoins() {
        return api.getCoins();
    }
}
