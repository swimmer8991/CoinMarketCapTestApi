package com.vlad.coinmarketcaptestapi.dagger.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.vlad.coinmarketcaptestapi.BuildConfig;
import com.vlad.coinmarketcaptestapi.MainActivity;
import com.vlad.coinmarketcaptestapi.api.CoinMarketCapApi;
import com.vlad.coinmarketcaptestapi.database.CoinMarketCapDatabase;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private Context appContext;

    public AppModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    public Context getAppContext() {
        return appContext;
    }

    @Provides
    @Singleton
    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    @Provides
    @Singleton
    OkHttpClient getClient() {
        return createDefaultClient();
    }

    @Provides
    @Singleton
    public CoinMarketCapApi getApi(OkHttpClient client) {
        return createRetrofit(client, BuildConfig.API_URL).create(CoinMarketCapApi.class);
    }

    @Provides
    @Singleton
    public CoinMarketCapDatabase getDatabase(Context context) {
        return Room.databaseBuilder(context, CoinMarketCapDatabase.class, "coinmarketcap")
                .build();
    }

    private Retrofit createRetrofit(OkHttpClient client, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    @SuppressWarnings("Convert2Lambda")
    private OkHttpClient createDefaultClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            @NonNull
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("X-CMC_PRO_API_KEY", BuildConfig.API_KEY);
                return chain.proceed(builder.build());
            }
        });
        return builder.build();
    }

}
