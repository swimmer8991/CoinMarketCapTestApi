package com.vlad.coinmarketcaptestapi.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.vlad.coinmarketcaptestapi.App;
import com.vlad.coinmarketcaptestapi.R;
import com.vlad.coinmarketcaptestapi.model.Coin;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinMarketCapDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_exchange_rate_to_btc) TextView exchangeRateToBtcTextView;
    @BindView(R.id.tv_exchange_rate_to_usd) TextView exchangeRateToUsdTextView;
    @BindView(R.id.tv_total_coins_available) TextView totalCoinsAvailableTextView;
    @BindView(R.id.tv_max_coins_available) TextView maxCoinsAvailableTextView;
    @BindView(R.id.tv_total_market_capitalization) TextView totalMarketCapitalizationTextView;
    @BindView(R.id.tv_percent_change_1h) TextView percentChange1hTextView;
    @BindView(R.id.tv_percent_change_24h) TextView percentChange24hTextView;
    @BindView(R.id.tv_percent_change_7d) TextView percentChange7dTextView;

    @Inject SharedPreferences sharedPreferences;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_market_cap_details);

        ButterKnife.bind(this);

        ((App) getApplicationContext()).getAppComponent().inject(this);

        Coin coin = (Coin) getIntent().getSerializableExtra(App.PUT_EXTRA_COIN);
        if (coin != null) {
            exchangeRateToBtcTextView.setText(String.format("%.6f", coin.getQuote().getUsd().getPrice()/getBtcPrice()));
            exchangeRateToUsdTextView.setText(String.format("%.6f",coin.getQuote().getUsd().getPrice()));
            totalCoinsAvailableTextView.setText(String.format("%.0f",coin.getTotalSupply()));
            maxCoinsAvailableTextView.setText(String.format("%.0f",coin.getMaxSupply()));
            totalMarketCapitalizationTextView.setText(String.format("%.0f",coin.getQuote().getUsd().getPrice() * coin.getTotalSupply()));

            if (coin.getQuote().getUsd().getPercentChange1h() > 0) {
                String percentChange1h = "+" + String.format("%.8f", coin.getQuote().getUsd().getPercentChange1h());
                percentChange1hTextView.setText(getSpannableStringWithGreenPlus(percentChange1h));
            } else {
                String percentChange1h = String.format("%.8f", coin.getQuote().getUsd().getPercentChange1h());
                percentChange1hTextView.setText(getSpannableStringWithRedMinus(percentChange1h));
            }

            if (coin.getQuote().getUsd().getPercentChange24h() > 0) {
                String percentChange24h = "+" + String.format("%.8f", coin.getQuote().getUsd().getPercentChange24h());
                percentChange24hTextView.setText(getSpannableStringWithGreenPlus(percentChange24h));
            } else {
                String percentChange24h = String.format("%.8f", coin.getQuote().getUsd().getPercentChange24h());
                percentChange24hTextView.setText(getSpannableStringWithRedMinus(percentChange24h));
            }

            if (coin.getQuote().getUsd().getPercentChange7d() > 0) {
                String percentChange7d = "+" + String.format("%.8f", coin.getQuote().getUsd().getPercentChange7d());
                percentChange7dTextView.setText(getSpannableStringWithGreenPlus(percentChange7d));
            } else {
                String percentChange7d = String.format("%.8f", coin.getQuote().getUsd().getPercentChange7d());
                percentChange7dTextView.setText(getSpannableStringWithRedMinus(percentChange7d));
            }
        }
    }

    private SpannableString getSpannableStringWithGreenPlus(String percentChange) {
        SpannableString spannableString = new SpannableString(percentChange);
        spannableString.setSpan(new RelativeSizeSpan(1.5f), 0,1, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 1, 0);
        return spannableString;
    }

    private SpannableString getSpannableStringWithRedMinus(String percentChange) {
        SpannableString spannableString = new SpannableString(percentChange);
        spannableString.setSpan(new RelativeSizeSpan(1.5f), 0,1, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, 0);
        return spannableString;
    }

    private float getBtcPrice() {
        return sharedPreferences.getFloat(App.PREF_BTC_PRICE, 0f);
    }

}