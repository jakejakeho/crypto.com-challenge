package org.example;

import org.example.marketData.MarketDataMessage;
import org.example.marketData.MarketDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

@Service
public class PortfolioResultSubscriber {
    Logger log = LoggerFactory.getLogger(PortfolioResultSubscriber.class);

    private final MarketDataProvider marketDataProvider;

    private final PositionReader positionReader;

    public PortfolioResultSubscriber(PositionReader positionReader, MarketDataProvider marketDataProvider) {
        this.positionReader = positionReader;
        this.marketDataProvider = marketDataProvider;
    }

    @PostConstruct
    public void subscribe() {
        marketDataProvider.subscribe(getConsumer());
    }

    private Consumer<MarketDataMessage> getConsumer() {
        return message -> {
            log.info("received message from mock market data provider" + message);
        };
    }
}
