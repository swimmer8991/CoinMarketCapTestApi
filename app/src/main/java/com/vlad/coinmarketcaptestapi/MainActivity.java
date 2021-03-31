package com.vlad.coinmarketcaptestapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.vlad.coinmarketcaptestapi.adapters.CoinMarketCapAdapter;
import com.vlad.coinmarketcaptestapi.database.CoinMarketCapStorageProvider;
import com.vlad.coinmarketcaptestapi.model.Coin;
import com.vlad.coinmarketcaptestapi.screens.CoinMarketCapDetailsActivity;
import com.vlad.coinmarketcaptestapi.utils.Lists;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.search_input) TextInputLayout searchTextInputLayout;
    @BindView(R.id.et_search) EditText searchEditText;
    @BindView(R.id.iv_search) ImageView searchImageView;
    @BindView(R.id.rv_coins) RecyclerView coinsRecyclerView;
    @BindView(R.id.swipe_layout_rv_coins) SwipeRefreshLayout coinsRecyclerViewSwipeLayout;
    @BindView(R.id.tv_update_status) TextView updateStatusTextView;
    @BindView(R.id.tv_try_again_hint) TextView tryAgainTextView;
    @BindView(R.id.bt_try_again) Button tryAgainButton;

    @Inject AppProviders appProviders;
    @Inject CoinMarketCapStorageProvider storageProvider;
    @Inject SharedPreferences sharedPreferences;
    CoinMarketCapAdapter coinMarketCapAdapter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private BehaviorSubject<List<Coin>> itemsSubject = BehaviorSubject.create();

    private boolean showTryAgain;

    private String pattern = "";

    {
        itemsSubject.onNext(Collections.emptyList());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ((App) getApplicationContext()).getAppComponent().inject(this);

        setupList();
        setupSwipeLayout();
        setupInputField();

        showTryAgain(true);
        loadCoins();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compositeDisposable.dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCoinsWithFiveMinutesIntervalAndUpdateStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @OnClick(R.id.bt_try_again)
    public void onTryAgainClick() {
        loadCoins();
    }

    private void showTryAgain(boolean show) {
        int visibilityTryAgain = show ? View.VISIBLE : View.GONE;
        int visibility = show ? View.GONE : View.VISIBLE;
        tryAgainTextView.setVisibility(visibilityTryAgain);
        tryAgainButton.setVisibility(visibilityTryAgain);
        searchTextInputLayout.setVisibility(visibility);
        coinsRecyclerView.setVisibility(visibility);
        searchImageView.setVisibility(visibility);
        updateStatus(!show);
        showTryAgain = show;
    }

    private void setupInputField() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) searchEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                pattern = s.toString();
                getCompositeDisposable().add(
                        loadCoins()
                                .map(items -> Lists.filter(items, coin -> coin.matches(pattern)))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(items -> {
                                    coinMarketCapAdapter.submitList(items);
                                    coinsRecyclerViewSwipeLayout.setRefreshing(false);
                                }, error -> Log.d(MainActivity.class.getSimpleName(), "Error: " + error.getCause()))

                );
            }
        });
    }

    private void setupList() {
        coinMarketCapAdapter = new CoinMarketCapAdapter(coin -> {
            Intent intent = new Intent(MainActivity.this, CoinMarketCapDetailsActivity.class);
            intent.putExtra(App.PUT_EXTRA_COIN, coin);
            MainActivity.this.startActivity(intent);
        });

        LinearLayoutManager listLayoutManager = new LinearLayoutManager(this);
        coinsRecyclerView.setLayoutManager(listLayoutManager);
        coinsRecyclerView.setAdapter(coinMarketCapAdapter);
    }

    private Observable<List<Coin>> loadCoins() {
        List<Coin> items = itemsSubject.getValue();
        if (items.isEmpty()) {
            loadCoinsFromStorageOrFromNetwork();
        }
        return itemsSubject;
    }

    private void loadCoinsFromStorageOrFromNetwork() {
        getCompositeDisposable().add(storageProvider.getCount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    if (count == 0) {
                        loadCoinsFromNetwork();
                    } else {
                        loadCoinsFromStorage();
                    }
                }, error -> Log.d(MainActivity.class.getSimpleName(), "Error: " + error.getCause()))
        );
    }

    private void loadCoinsFromStorage() {
        getCompositeDisposable().add(storageProvider.getCoinsList()
                .doOnSuccess(itemsSubject::onNext)
                .map(items -> Lists.filter(items, coin -> coin.matches(pattern)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    coinMarketCapAdapter.submitList(items);
                    coinsRecyclerViewSwipeLayout.setRefreshing(false);
                    showTryAgain(false);
                }, error -> {}));
    }

    private void loadCoinsFromNetwork() {
        getCompositeDisposable().add(
                appProviders.getCoinMarketCapProvider().loadCoins()
                        .map(coins -> {
                            for (Coin coin : coins.getData()) {
                                if (coin.getId() == 1) {
                                    setBtcPrice(coin.getQuote().getUsd().getPrice());
                                }
                            }
                            return coins;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(coins -> {
                            setLastLoadDate(coins.getStatus().getTimestamp().getTime());
                            compositeDisposable.add(storageProvider.saveCoinsList(coins.getData())
                                    .subscribe(
                                            this::loadCoinsFromStorage,
                                            error -> Log.d(MainActivity.class.getSimpleName(), "Error: " + error.getCause())
                                    )
                            );
                            },
                        error -> {
                            Log.d(MainActivity.class.getSimpleName(), "Error: " + error.getCause());
                            coinsRecyclerViewSwipeLayout.setRefreshing(false);
                            }
                        )
        );
    }

    private void loadCoinsWithFiveMinutesIntervalAndUpdateStatus() {
        getCompositeDisposable().add(Observable.interval(1L, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    long delay = TimeUnit.MILLISECONDS.toMinutes(Math.abs(new Date().getTime() - getLastLoadDate()));
                    if (delay >= 5L) {
                        loadCoinsFromNetwork();
                    }
                    if (!showTryAgain) {
                        updateStatus(true);
                    }
                }, err -> {})
        );
    }

    private void updateStatus(boolean show) {
        long updateStatus = TimeUnit.MILLISECONDS.toMinutes(Math.abs(new Date().getTime() - getLastLoadDate()));
        if (updateStatus == 0L || !show) {
            updateStatusTextView.setVisibility(View.GONE);
        } else if (updateStatus < 60){
            String message = getResources().getQuantityString(R.plurals.update_status_minutes, (int) updateStatus, updateStatus);
            updateStatusTextView.setVisibility(View.VISIBLE);
            updateStatusTextView.setText(message);
        } else {
            String message = getResources().getQuantityString(R.plurals.update_status_hours, (int) TimeUnit.MINUTES.toHours(updateStatus), TimeUnit.MINUTES.toHours(updateStatus));
            updateStatusTextView.setVisibility(View.VISIBLE);
            updateStatusTextView.setText(message);
        }
    }

    private void setupSwipeLayout() {
        coinsRecyclerViewSwipeLayout.setOnRefreshListener(this::loadCoinsFromNetwork);
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    public void setLastLoadDate(long lastUploadDate) {
        sharedPreferences.edit()
                .putLong(App.PREF_LAST_LOAD_DATE, lastUploadDate)
                .apply();
    }

    public long getLastLoadDate() {
        return sharedPreferences.getLong(App.PREF_LAST_LOAD_DATE, new Date().getTime());
    }

    public void setBtcPrice(float btcPrice) {
        sharedPreferences.edit()
                .putFloat(App.PREF_BTC_PRICE, btcPrice)
                .apply();
    }

}