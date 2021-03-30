package com.vlad.coinmarketcaptestapi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vlad.coinmarketcaptestapi.adapters.HasId;
import com.vlad.coinmarketcaptestapi.database.CoinEntity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Coin implements HasId, Serializable {

    @SerializedName("id")
    @Expose
    private final long id;

    @SerializedName("name")
    @Expose
    private final String name;

    @SerializedName("symbol")
    @Expose
    private final String symbol;

    @SerializedName("max_supply")
    @Expose
    private final Float maxSupply;

    @SerializedName("total_supply")
    @Expose
    private final Float totalSupply;

    @SerializedName("cmc_rank")
    @Expose
    private final Integer cmcRank;

    @SerializedName("quote")
    @Expose
    private final Quote quote;

    public CoinEntity toEntity() {
        CoinEntity coinEntity = new CoinEntity();
        coinEntity.id = id;
        coinEntity.name = name;
        coinEntity.symbol = symbol;
        coinEntity.maxSupply = maxSupply;
        coinEntity.totalSupply = totalSupply;
        coinEntity.cmcRank = cmcRank;
        coinEntity.price = quote.getUsd().getPrice();
        coinEntity.percentChange1h = quote.getUsd().getPercentChange1h();
        coinEntity.percentChange7d = quote.getUsd().getPercentChange7d();
        coinEntity.percentChange24h = quote.getUsd().getPercentChange24h();
        return coinEntity;
    }

    public static Coin fromEntity(CoinEntity coinEntity) {
        USD usd = new USD(
                coinEntity.price,
                coinEntity.percentChange1h,
                coinEntity.percentChange24h,
                coinEntity.percentChange7d
        );
        Quote quote = new Quote(usd);
        return new Coin(
                coinEntity.id,
                coinEntity.name,
                coinEntity.symbol,
                coinEntity.maxSupply,
                coinEntity.totalSupply,
                coinEntity.cmcRank,
                quote
        );
    }

    public boolean matches(String pattern) {
        String name = getName().toUpperCase();
        String symbol = getSymbol().toUpperCase();
        return name.contains(pattern.toUpperCase()) || symbol.contains(pattern.toUpperCase());
    }

}
