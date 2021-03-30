package com.vlad.coinmarketcaptestapi.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vlad.coinmarketcaptestapi.R;
import com.vlad.coinmarketcaptestapi.model.Coin;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinMarketCapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<Coin> items = Collections.emptyList();

    private CoinMarketCapActionListener listener;

    public CoinMarketCapAdapter(CoinMarketCapActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CoinMarketCapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_coin, parent, false);
        return new CoinMarketCapViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder binding, int position) {
        bind((CoinMarketCapViewHolder) binding, items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View view) {
        Coin coin = (Coin) view.getTag();
        listener.onItemClick(coin);
    }

    public void submitList(List<Coin> items) {
        if (this.items.equals(items)) return;

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new SimpleDiffCallback<>(this.items, items));

        this.items = items;

        result.dispatchUpdatesTo(this);
    }

    public void bind(CoinMarketCapViewHolder binding, Coin coin) {
        binding.currencyNameTextView.setText(coin.getName());
        binding.currencyCodeTextView.setText(coin.getSymbol());
        binding.actualExchangeRateToUsdTextView.setText(String.valueOf(coin.getQuote().getUsd().getPrice()));

        binding.itemView.setTag(coin);
    }

    static class CoinMarketCapViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_currency_name) TextView currencyNameTextView;
        @BindView(R.id.tv_currency_code) TextView currencyCodeTextView;
        @BindView(R.id.tv_actual_exchange_rate_to_usd) TextView actualExchangeRateToUsdTextView;

        public CoinMarketCapViewHolder(@NonNull View itemView, View.OnClickListener listener) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(listener);
        }
    }

    public interface CoinMarketCapActionListener {
        void onItemClick(Coin coin);
    }
}
