package org.example.marketData.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Service
public class StockMarketDataProvider {

    private final Logger log = LoggerFactory.getLogger(StockMarketDataProvider.class);

    private final List<Consumer<StockMarketDataMessage>> consumers = Collections.synchronizedList(new ArrayList<>());

    ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();

    public void subscribe(Consumer<StockMarketDataMessage> consumer) {
        consumers.add(consumer);
    }

    private final AtomicReference<Integer> messageId = new AtomicReference<>(1);

    @Scheduled(fixedRate = 10000L, initialDelay = 0L)
    private void publish() {
        StockMarketDataMessage stockMarketDataMessage = getRandomStockMarketData();
        for (Consumer<StockMarketDataMessage> consumer : consumers) {
            CompletableFuture.runAsync(() -> {
                log.info("mock market data provider public message " + stockMarketDataMessage);
                consumer.accept(stockMarketDataMessage);
            }, threadPoolExecutor);
        }
    }

    private StockMarketDataMessage getRandomStockMarketData() {
        StockMarketDataMessage stockMarketDataMessage = new StockMarketDataMessage();
        stockMarketDataMessage.setMessageId(messageId.getAndAccumulate(1, Integer::sum));
        stockMarketDataMessage.setChanges(new ArrayList<>());
        StockMarketDataMessage.StockMarketChange change1 = new StockMarketDataMessage.StockMarketChange();
        change1.setSymbol("AAPL");
        change1.setLatestPrice(BigDecimal.valueOf(randDouble(1, 100)).setScale(2, RoundingMode.UP));
        stockMarketDataMessage.getChanges().add(change1);

        StockMarketDataMessage.StockMarketChange change2 = new StockMarketDataMessage.StockMarketChange();
        change2.setSymbol("TESLA");
        change2.setLatestPrice(BigDecimal.valueOf(randDouble(300, 400)).setScale(2, RoundingMode.UP));
        stockMarketDataMessage.getChanges().add(change2);
        return stockMarketDataMessage;
    }

    private double randDouble(double bound1, double bound2) {
        double min = Math.min(bound1, bound2);
        double max = Math.max(bound1, bound2);
        return min + (Math.random() * (max - min));
    }
}
