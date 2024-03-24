package org.example.option;

import org.example.stock.StockMarketDataMessage;
import org.example.stock.StockMarketDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class OptionMarketDataProvider {

    Logger log = LoggerFactory.getLogger(OptionMarketDataProvider.class);

    private List<Consumer<OptionMarketDataMessage>> consumers = Collections.synchronizedList(new ArrayList<>());

    private ExecutorService consumerThreadPool = Executors.newSingleThreadExecutor();

    private ExecutorService publisherThreadPool = Executors.newSingleThreadExecutor();

    private final StockMarketDataProvider stockMarketDataProvider;
    private final OptionPriceCalculator optionPriceCalculator;

    public OptionMarketDataProvider(StockMarketDataProvider stockMarketDataProvider, OptionPriceCalculator optionPriceCalculator) {
        this.stockMarketDataProvider = stockMarketDataProvider;
        this.optionPriceCalculator = optionPriceCalculator;
    }

    public void subscribe(Consumer<OptionMarketDataMessage> consumer) {
        consumers.add(consumer);
    }

    @PostConstruct
    private void subscribeStockPrice() {
        stockMarketDataProvider.subscribe(getConsumer());
    }

    private Consumer<StockMarketDataMessage> getConsumer() {
        return stockMarketDataMessage -> CompletableFuture.runAsync(() -> {
            BigDecimal optionPrice = optionPriceCalculator.getOptionPrice(stockMarketDataMessage.getSymbol(), stockMarketDataMessage.getLatestPrice());
            log.info(stockMarketDataMessage + " option price = " + optionPrice);
            publishOptionPrice(stockMarketDataMessage.getSymbol(), optionPrice);
        }, consumerThreadPool);
    }

    private void publishOptionPrice(String symbol, BigDecimal optionPrice) {
        OptionMarketDataMessage optionMarketDataMessage = new OptionMarketDataMessage();
        optionMarketDataMessage.setSymbol(symbol);
        optionMarketDataMessage.setLatestPrice(optionPrice);
        for (Consumer<OptionMarketDataMessage> consumer : consumers) {
            CompletableFuture.runAsync(() -> {
                consumer.accept(optionMarketDataMessage);
            }, publisherThreadPool);
        }
    }
}