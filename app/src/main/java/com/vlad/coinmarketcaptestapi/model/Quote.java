package com.vlad.coinmarketcaptestapi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Quote implements Serializable {

    @SerializedName("USD")
    @Expose
    private final USD usd;

}
