package org.example.protfolio;

import org.example.marketData.option.OptionMarketDataMessage;
import org.example.marketData.option.OptionMarketDataProvider;
import org.example.marketData.stock.StockMarketDataMessage;
import org.example.marketData.stock.StockMarketDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class PortfolioProvider {

    private final StockMarketDataProvider stockMarketDataProvider;

    private final OptionMarketDataProvider optionMarketDataProvider;


    public PortfolioProvider(StockMarketDataProvider stockMarketDataProvider, OptionMarketDataProvider optionMarketDataProvider) {
        this.stockMarketDataProvider = stockMarketDataProvider;
        this.optionMarketDataProvider = optionMarketDataProvider;
    }

    private final Logger log = LoggerFactory.getLogger(PortfolioProvider.class);

    private List<Consumer<PortfolioDTO>> consumers = Collections.synchronizedList(new ArrayList<>());

    private ExecutorService publishThreadPool = Executors.newSingleThreadExecutor();

    public void subscribe(Consumer<PortfolioDTO> consumer) {
        consumers.add(consumer);
    }

    @PostConstruct
    private void subscribePrice() {
    }

    private void publishPortfolio() {
        for (Consumer<PortfolioDTO> consumer : consumers) {
            CompletableFuture.runAsync(() -> consumer.accept(calculatePortfolio()), publishThreadPool);
        }
    }

    private PortfolioDTO calculatePortfolio() {
        return new PortfolioDTO();
    }
}
