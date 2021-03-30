package com.vlad.coinmarketcaptestapi;

import android.app.Application;
import android.util.Log;

import com.vlad.coinmarketcaptestapi.dagger.components.AppComponent;
import com.vlad.coinmarketcaptestapi.dagger.components.DaggerAppComponent;
import com.vlad.coinmarketcaptestapi.dagger.modules.AppModule;
import com.vlad.coinmarketcaptestapi.dagger.modules.ProvidersModule;

public class App extends Application {

    public static final String PREF_LAST_LOAD_DATE = "PREF_LAST_LOAD_DATE";
    public static final String PREF_BTC_PRICE = "PREF_BTC_PRICE";
    public static final String PUT_EXTRA_COIN = "PUT_EXTRA_COIN";

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .providersModule(new ProvidersModule())
                .build();
        Log.d(App.class.getSimpleName(), "Version: " + BuildConfig.VERSION_NAME);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
