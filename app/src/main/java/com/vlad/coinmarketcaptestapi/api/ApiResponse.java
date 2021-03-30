package com.vlad.coinmarketcaptestapi.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vlad.coinmarketcaptestapi.model.Status;

import lombok.Data;

@Data
public class ApiResponse<T> {

    @SerializedName("status")
    @Expose
    private final Status status;
    @SerializedName("data")
    @Expose
    private final T data;

}
