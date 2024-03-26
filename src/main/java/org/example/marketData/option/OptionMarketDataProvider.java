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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Service
public class OptionMarketDataProvider {

    private final Logger log = LoggerFactory.getLogger(OptionMarketDataProvider.class);

    private List<Consumer<OptionMarketDataMessage>> consumers = Collections.synchronizedList(new ArrayList<>());

    private ExecutorService consumerThreadPool = Executors.newSingleThreadExecutor();

    private ExecutorService publisherThreadPool = Executors.newSingleThreadExecutor();

    private final StockMarketDataProvider stockMarketDataProvider;
    private final OptionPriceCalculator optionPriceCalculator;
    private final SecurityService securityService;

    public OptionMarketDataProvider(StockMarketDataProvider stockMarketDataProvider, SecurityService securityService) {
        this.stockMarketDataProvider = stockMarketDataProvider;
        this.optionPriceCalculator = new OptionPriceCalculator();
        this.securityService = securityService;
    }

    public void subscribe(Consumer<OptionMarketDataMessage> consumer) {
        consumers.add(consumer);
    }

    @PostConstruct
    private void subscribeStockPrice() {
        stockMarketDataProvider.subscribe(this::processStockPrice);
    }

    private void processStockPrice(StockMarketDataMessage stockMarketDataMessage) {
        OptionMarketDataMessage optionMarketDataMessage = new OptionMarketDataMessage();
        optionMarketDataMessage.setMessageId(stockMarketDataMessage.getMessageId());
        optionMarketDataMessage.setChanges(new ArrayList<>());

        for (StockMarketDataMessage.StockMarketChange stockMarketChange : stockMarketDataMessage.getChanges()) {
            List<Security> options = securityService.findAllOptionsBySymbol(stockMarketChange.getSymbol());
            for (Security option : options) {
                BigDecimal optionPrice = optionPriceCalculator.getOptionPrice(option, stockMarketChange.getLatestPrice());
                log.info(stockMarketDataMessage + "Option {}: price={}", option, optionPrice);
                optionMarketDataMessage.getChanges().add(new OptionMarketDataMessage.OptionMarketDataChange(option.getSymbol(), optionPrice));
            }
        }
        log.info("after process stock price: {}", optionMarketDataMessage);
        publishOptionPrice(optionMarketDataMessage);
    }

    private void publishOptionPrice(OptionMarketDataMessage message) {
        for (Consumer<OptionMarketDataMessage> consumer : consumers) {
            CompletableFuture.runAsync(() -> consumer.accept(message), publisherThreadPool);
        }
    }
}