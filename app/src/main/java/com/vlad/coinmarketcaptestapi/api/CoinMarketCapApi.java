package com.vlad.coinmarketcaptestapi.api;

import com.vlad.coinmarketcaptestapi.model.Coin;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinMarketCapApi {

    @GET("v1/cryptocurrency/listings/latest")
    Single<ApiResponse<List<Coin>>> getCoins();

}
