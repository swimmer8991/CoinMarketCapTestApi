package com.vlad.coinmarketcaptestapi.database;

import com.vlad.coinmarketcaptestapi.model.Coin;
import com.vlad.coinmarketcaptestapi.utils.Lists;

import java.util.List;

import dagger.Lazy;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class CoinMarketCapStorageProvider {

    private Lazy<CoinMarketCapDatabase> database;

    public CoinMarketCapStorageProvider(Lazy<CoinMarketCapDatabase> database) {
        this.database = database;
    }

    public Single<Integer> getCount() {
        return getDao().getCount()
                .subscribeOn(Schedulers.io());
    }

    public Single<List<Coin>> getCoinsList() {
        return getDao().getCoinsList()
                .map(coinEntities -> Lists.map(coinEntities, Coin::fromEntity))
                .subscribeOn(Schedulers.io());
    }

    public Single<List<Coin>> getCoinsByNameOrSymbol(String pattern) {
        return getDao().getCoinsByNameOrSymbol(pattern)
                .map(coinEntities -> Lists.map(coinEntities, Coin::fromEntity))
                .subscribeOn(Schedulers.io());
    }

    public Completable saveCoin(Coin coin) {
        return Completable.fromRunnable(() -> getDao().insert(coin.toEntity()))
                .subscribeOn(Schedulers.io());
    }

    public Completable saveCoinsList(List<Coin> coinList) {
        List<CoinEntity> coinEntityList = Lists.map(coinList, Coin::toEntity);
        return Completable.fromRunnable(() -> getDao().insertList(coinEntityList))
                .subscribeOn(Schedulers.io());
    }

    private CoinsDao getDao() {
        return database.get().getCoinsDao();
    }
}
