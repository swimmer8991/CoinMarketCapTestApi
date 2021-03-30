package com.vlad.coinmarketcaptestapi.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {
                CoinEntity.class
        },
        version = 1
)
public abstract class CoinMarketCapDatabase extends RoomDatabase {

    public abstract CoinsDao getCoinsDao();

}
