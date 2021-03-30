package com.vlad.coinmarketcaptestapi.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.EqualsAndHashCode;

@Entity(tableName = "coins_list")
@EqualsAndHashCode
public class CoinEntity {

    @PrimaryKey public long id;

    @ColumnInfo(name = "name") public String name;

    @ColumnInfo(name = "symbol") public String symbol;

    @ColumnInfo(name = "max_supply") public Float maxSupply;

    @ColumnInfo(name = "total_supply") public Float totalSupply;

    @ColumnInfo(name = "cmc_rank") public Integer cmcRank;

    @ColumnInfo(name = "price") public Float price;

    @ColumnInfo(name = "percent_change_1h") public Float percentChange1h;

    @ColumnInfo(name = "percent_change_24h") public Float percentChange24h;

    @ColumnInfo(name = "percent_change_7d") public Float percentChange7d;

}
