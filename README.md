# CoinMarketCap Test App

An Android application developed in Java that tracks cryptocurrency market data using the CoinMarketCap API. The project demonstrates a robust implementation of local caching, reactive data streams, and automated background synchronization.

---

## 🚀 Key Features

* **Real-time Cryptocurrency List:** Displays coin names, symbols, and current prices in USD fetched from the latest listings.
* **Offline Support:** Implements a local Room database to cache data, ensuring functionality without an active internet connection.
* **Automatic Data Refresh:** Includes logic to automatically fetch fresh data from the network every 5 minutes while the app is active.
* **Update Status Tracking:** Notifies users when data was last updated by displaying time elapsed (e.g., minutes or hours ago) based on stored timestamps.
* **Live Search & Filtering:** Allows users to filter the coin list in real-time by coin name using a search input field.
* **Efficient UI Updates:** Uses `DiffUtil` to update only the changed items in the list, providing smooth animations and high performance.

---

## 🛠 Tech Stack

* **Language:** Java.
* **Dependency Injection:** [Dagger 2](https://dagger.dev/) for managing global providers and application components.
* **Networking:** [Retrofit 2](https://square.github.io/retrofit/) for API communication with the CoinMarketCap endpoint.
* **Database:** [Room Persistence Library](https://developer.android.com/training/data-storage/room) for local SQLite storage.
* **Reactive Programming:** [RxJava 2](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid) to handle asynchronous data flows and UI updates.
* **View Binding:** [ButterKnife](http://jakewharton.github.io/butterknife/) for view injection and click event handling.
* **Data Handling:** [Lombok](https://projectlombok.org/) for reducing boilerplate code like getters, setters, and equals/hashcode.
* **JSON Parsing:** [GSON](https://github.com/google/gson) for serializing and deserializing API responses and entity mapping.

---

## 🏗 Architecture & Core Logic

### Data Layer

* **API Service (`CoinMarketCapApi`):** Defines the GET request for `/v1/cryptocurrency/listings/latest` returning a `Single` reactive type.
* **Storage (`CoinMarketCapDatabase`):** An abstract Room database that provides access to the `CoinsDao` for managing `CoinEntity` objects.
* **Entities (`CoinEntity`):** Represents the database schema, storing properties like price, supply, and 1h/24h/7d percentage changes.

### UI & Adapters

* **MainActivity:** The primary entry point that manages the lifecycle, handles the search logic, and coordinates between network and local storage.
* **CoinMarketCapAdapter:** Manages the RecyclerView, binding coin data to UI components and handling item clicks.
* **SimpleDiffCallback:** A generic implementation of `DiffUtil.Callback` that uses a `HasId` interface to identify unique items for optimized list refreshes.

### Dependency Injection

* **AppComponent:** The core Dagger component that injects dependencies into `MainActivity`.
* **AppProviders:** A singleton class injected with the `CoinMarketCapProvider` to centralize data access.

---

## 📋 Implementation Details

### Automated Synchronization

The application checks the last load date stored in `SharedPreferences`. If more than 5 minutes have elapsed, it triggers a background network call to update the local database:

```java
// Logic for interval checking found in MainActivity.java
long delay = TimeUnit.MILLISECONDS.toMinutes(Math.abs(new Date().getTime() - getLastLoadDate()));
if (delay >= 5L) {
    loadCoinsFromNetwork();
}

```

### Search Logic

The search functionality uses a `TextWatcher` to update a search pattern. It then filters the list of coins using a utility class and updates the adapter via RxJava:

```java
// Filtering logic in MainActivity.java
loadCoins()
    .map(items -> Lists.filter(items, coin -> coin.matches(pattern)))
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(items -> coinMarketCapAdapter.submitList(items));

```
