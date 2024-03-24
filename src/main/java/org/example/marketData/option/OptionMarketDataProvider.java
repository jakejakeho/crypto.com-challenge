package org.example.marketData.option;

import org.example.security.Security;
import org.example.security.SecurityService;
import org.example.marketData.stock.StockMarketDataMessage;
import org.example.marketData.stock.StockMarketDataProvider;
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

    private List<Consumer<List<OptionMarketDataMessage>>> consumers = Collections.synchronizedList(new ArrayList<>());

    private ExecutorService consumerThreadPool = Executors.newSingleThreadExecutor();

    private ExecutorService publisherThreadPool = Executors.newSingleThreadExecutor();

    private final StockMarketDataProvider stockMarketDataProvider;
    private final OptionPriceCalculator optionPriceCalculator;
    private final SecurityService securityService;

    public OptionMarketDataProvider(StockMarketDataProvider stockMarketDataProvider, OptionPriceCalculator optionPriceCalculator, SecurityService securityService) {
        this.stockMarketDataProvider = stockMarketDataProvider;
        this.optionPriceCalculator = optionPriceCalculator;
        this.securityService = securityService;
    }

    public void subscribe(Consumer<List<OptionMarketDataMessage>> consumer) {
        consumers.add(consumer);
    }

    @PostConstruct
    private void subscribeStockPrice() {
        stockMarketDataProvider.subscribe(getConsumer());
    }

    private Consumer<List<StockMarketDataMessage>> getConsumer() {
        return stockMarketDataMessage -> CompletableFuture.runAsync(() -> processStockPrice(stockMarketDataMessage), consumerThreadPool);
    }

    private void processStockPrice(List<StockMarketDataMessage> stockMarketDataMessages) {
        List<OptionMarketDataMessage> optionMarketDataMessages = new ArrayList<>();
        for (StockMarketDataMessage stockMarketDataMessage : stockMarketDataMessages) {
            List<Security> options = securityService.findAllOptionsBySymbol(stockMarketDataMessage.getSymbol());
            for (Security option : options) {
                BigDecimal optionPrice = optionPriceCalculator.getOptionPrice(option, stockMarketDataMessage.getLatestPrice());
                log.info(stockMarketDataMessage + "Option {}: price: = ", stockMarketDataMessage.getSymbol(), optionPrice);
                optionMarketDataMessages.add(new OptionMarketDataMessage(option.getSymbol(), optionPrice));
            }
        }
        publishOptionPrice(optionMarketDataMessages);
    }

    private void publishOptionPrice(List<OptionMarketDataMessage> messages) {
        for (Consumer<List<OptionMarketDataMessage>> consumer : consumers) {
            CompletableFuture.runAsync(() -> consumer.accept(messages), publisherThreadPool);
        }
    }
}