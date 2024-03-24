package org.example;

import org.example.marketData.StockMarketDataMessage;
import org.example.marketData.StockMarketDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

@Service
public class PortfolioResultSubscriber {
    Logger log = LoggerFactory.getLogger(PortfolioResultSubscriber.class);

    private final StockMarketDataProvider stockMarketDataProvider;

    private final PositionReader positionReader;

    public PortfolioResultSubscriber(PositionReader positionReader, StockMarketDataProvider stockMarketDataProvider) {
        this.positionReader = positionReader;
        this.stockMarketDataProvider = stockMarketDataProvider;
    }

    @PostConstruct
    public void subscribe() {
        // TODO: async receive
        stockMarketDataProvider.subscribe(getConsumer());
    }

    private Consumer<StockMarketDataMessage> getConsumer() {
        return message -> {
            log.info("received message from mock market data provider" + message);
        };
    }
}
