package org.example.marketData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

@Service
public class OptionMarketDataProvider {

    Logger log = LoggerFactory.getLogger(OptionMarketDataProvider.class);

    private final StockMarketDataProvider stockMarketDataProvider;

    public OptionMarketDataProvider(StockMarketDataProvider stockMarketDataProvider) {
        this.stockMarketDataProvider = stockMarketDataProvider;
    }

    @PostConstruct
    public void subscribe() {
        // TODO: async receive
        stockMarketDataProvider.subscribe(getConsumer());
    }

    public Consumer<StockMarketDataMessage> getConsumer() {
        return stockMarketDataMessage -> {
            log.info("received stock price " + stockMarketDataMessage);
        };
    }

}

