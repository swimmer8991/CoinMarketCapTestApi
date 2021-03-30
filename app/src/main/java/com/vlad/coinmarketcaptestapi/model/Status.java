package com.vlad.coinmarketcaptestapi.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.security.Timestamp;
import java.util.Date;

import lombok.Data;

@Data
public class Status {

    @SerializedName("timestamp")
    @Expose
    private final Date timestamp;

    @SerializedName("error_code")
    @Expose
    private final Integer errorCode;

    @SerializedName("error_message")
    @Expose
    private final String errorMessage;

    @SerializedName("elapsed")
    @Expose
    private final Integer elapsed;

    @SerializedName("credit_count")
    @Expose
    private final Integer creditCount;

    @SerializedName("total_count")
    @Expose
    private final Integer totalCount;

}
