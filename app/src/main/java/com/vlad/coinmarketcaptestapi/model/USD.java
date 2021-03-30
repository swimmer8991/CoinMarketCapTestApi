package com.vlad.coinmarketcaptestapi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class USD implements Serializable {

    @SerializedName("price")
    @Expose
    private final Float price;

    @SerializedName("percent_change_1h")
    @Expose
    private final Float percentChange1h;

    @SerializedName("percent_change_24h")
    @Expose
    private final Float percentChange24h;

    @SerializedName("percent_change_7d")
    @Expose
    private final Float percentChange7d;

}
