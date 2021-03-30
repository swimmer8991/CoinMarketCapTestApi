package com.vlad.coinmarketcaptestapi.dagger.components;

import com.vlad.coinmarketcaptestapi.AppProviders;
import com.vlad.coinmarketcaptestapi.MainActivity;
import com.vlad.coinmarketcaptestapi.dagger.modules.AppModule;
import com.vlad.coinmarketcaptestapi.dagger.modules.ProvidersModule;
import com.vlad.coinmarketcaptestapi.screens.CoinMarketCapDetailsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ProvidersModule.class})
public interface AppComponent {

    AppProviders getAppProviders();

    void inject(MainActivity mainActivity);

    void inject(CoinMarketCapDetailsActivity coinMarketCapDetailsActivity);

}
