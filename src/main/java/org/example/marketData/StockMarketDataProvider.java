package org.example.marketData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Service
public class StockMarketDataProvider {

    Logger log = LoggerFactory.getLogger(StockMarketDataProvider.class);

    List<Consumer<StockMarketDataMessage>> consumers = Collections.synchronizedList(new ArrayList<>());

    public void subscribe(Consumer<StockMarketDataMessage> consumer) {
        consumers.add(consumer);
    }

    @Scheduled(fixedRate = 1000L)
    public void publish() {
        List<StockMarketDataMessage> messages = new ArrayList<>();
        StockMarketDataMessage message1 = new StockMarketDataMessage();
        message1.setSymbol("AAPL");
        message1.setLatestPrice(BigDecimal.valueOf(randDouble(1, 100)));
        messages.add(message1);
        // TODO: async publish
        for (Consumer<StockMarketDataMessage> consumer : consumers) {
            for (StockMarketDataMessage message : messages) {
                log.info("mock market data provider public message " + message);
                consumer.accept(message);
            }
        }
    }

    public double randDouble(double bound1, double bound2) {
        double min = Math.min(bound1, bound2);
        double max = Math.max(bound1, bound2);
        return min + (Math.random() * (max - min));
    }
}
