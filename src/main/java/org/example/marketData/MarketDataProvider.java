package org.example.marketData;


import org.checkerframework.checker.units.qual.C;
import org.example.marketData.option.OptionMarketDataMessage;
import org.example.marketData.option.OptionMarketDataProvider;
import org.example.marketData.stock.StockMarketDataMessage;
import org.example.marketData.stock.StockMarketDataProvider;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.Consumer;

@Service
public class MarketDataProvider {

    private final StockMarketDataProvider stockMarketDataProvider;

    private final OptionMarketDataProvider optionMarketDataProvider;

    public MarketDataProvider(StockMarketDataProvider stockMarketDataProvider, OptionMarketDataProvider optionMarketDataProvider) {
        this.stockMarketDataProvider = stockMarketDataProvider;
        this.optionMarketDataProvider = optionMarketDataProvider;
    }

    @PostConstruct
    private void subscribePrice() {
        stockMarketDataProvider.subscribe(getStockPriceConsumer());
        optionMarketDataProvider.subscribe(getOptionPriceConsumer());
    }

    private Consumer<List<StockMarketDataMessage>> getStockPriceConsumer() {
        return new Consumer<List<StockMarketDataMessage>>() {
            @Override
            public void accept(List<StockMarketDataMessage> stockMarketDataMessages) {

            }
        };
    }

    private Consumer<List<OptionMarketDataMessage>> getOptionPriceConsumer() {
        return new Consumer<List<OptionMarketDataMessage>>() {
            @Override
            public void accept(List<OptionMarketDataMessage> optionMarketDataMessages) {

            }
        };
    }
}
