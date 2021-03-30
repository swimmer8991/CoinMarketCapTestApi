package com.vlad.coinmarketcaptestapi.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface CoinsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CoinEntity coinEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<CoinEntity> coinEntityList);

    @Query("SELECT * FROM coins_list WHERE id = :id")
    Single<CoinEntity> getCoinById(long id);

    @Query("SELECT * FROM coins_list WHERE name = :pattern OR symbol = :pattern")
    Single<List<CoinEntity>> getCoinsByNameOrSymbol(String pattern);

    @Query("SELECT * FROM coins_list ORDER BY cmc_rank")
    Single<List<CoinEntity>> getCoinsList();

    @Query("SELECT COUNT(id) FROM coins_list")
    Single<Integer> getCount();
}
