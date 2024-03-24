package org.example.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class StockMarketDataProvider {

    private Logger log = LoggerFactory.getLogger(StockMarketDataProvider.class);

    private List<Consumer<StockMarketDataMessage>> consumers = Collections.synchronizedList(new ArrayList<>());

    ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();

    public void subscribe(Consumer<StockMarketDataMessage> consumer) {
        consumers.add(consumer);
    }

    @Scheduled(fixedRate = 10000L, initialDelay = 0L)
    private void publish() {
        List<StockMarketDataMessage> messages = new ArrayList<>();
        StockMarketDataMessage message1 = new StockMarketDataMessage();
        message1.setSymbol("AAPL");
        message1.setLatestPrice(BigDecimal.valueOf(randDouble(1, 100)));
        messages.add(message1);
        for (Consumer<StockMarketDataMessage> consumer : consumers) {
            for (StockMarketDataMessage message : messages) {
                CompletableFuture.runAsync(() -> {
                    log.info("mock market data provider public message " + message);
                    consumer.accept(message);
                }, threadPoolExecutor);
            }
        }
    }

    private double randDouble(double bound1, double bound2) {
        double min = Math.min(bound1, bound2);
        double max = Math.max(bound1, bound2);
        return min + (Math.random() * (max - min));
    }
}
