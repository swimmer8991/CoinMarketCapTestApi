package com.vlad.coinmarketcaptestapi.dagger.modules;

import com.vlad.coinmarketcaptestapi.api.CoinMarketCapApi;
import com.vlad.coinmarketcaptestapi.database.CoinMarketCapDatabase;
import com.vlad.coinmarketcaptestapi.database.CoinMarketCapStorageProvider;
import com.vlad.coinmarketcaptestapi.providers.CoinMarketCapProvider;

import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

@Module
public class ProvidersModule {

    @Provides
    @Singleton
    public CoinMarketCapProvider getCoinMarketCapProvider(CoinMarketCapApi api) {
        return new CoinMarketCapProvider(api);
    }

    @Provides
    @Singleton
    public CoinMarketCapStorageProvider getCoinMarketCapStorageProvider(Lazy<CoinMarketCapDatabase> database) {
        return new CoinMarketCapStorageProvider(database);
    }

}
