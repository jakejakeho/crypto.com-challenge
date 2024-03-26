package org.example.marketData.stock;

import org.example.security.Security;
import org.example.security.SecurityRepository;
import org.example.security.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.example.marketData.Constants.RISK_FREE_RATE;
import static org.example.marketData.Constants.VOLATILITY;

@Service
public class StockMarketDataProvider {

    private final SecurityService securityService;

    public StockMarketDataProvider(SecurityService securityService) {
        this.securityService = securityService;
    }

    private final Logger log = LoggerFactory.getLogger(StockMarketDataProvider.class);

    private final List<Consumer<StockMarketDataMessage>> consumers = Collections.synchronizedList(new ArrayList<>());

    ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();

    private final Map<String, Double> priceMap = new HashMap<>();

    private final Map<String, Random> randomMap = new HashMap<>();

    private static final int STEPS = 365;

    public void subscribe(Consumer<StockMarketDataMessage> consumer) {
        consumers.add(consumer);
    }

    private final AtomicReference<Integer> messageId = new AtomicReference<>(1);

    private double getRandomPrice(Security security) {
        double T = 1; // Time horizon
        double dt = T / STEPS; // Time step size

        randomMap.computeIfAbsent(security.getSymbol(), (k) -> new Random());
        Random rand = randomMap.get(security.getSymbol());

        int step = messageId.get() % 365;
        if (!priceMap.containsKey(security.getSymbol()) || step == 0)
            priceMap.put(security.getSymbol(), security.getInitialPrice());

        double Z = rand.nextGaussian();
        double newPrice = priceMap.get(security.getSymbol()) * Math.exp((RISK_FREE_RATE - 0.5 * VOLATILITY * VOLATILITY) * dt + VOLATILITY * Math.sqrt(dt) * Z);
        priceMap.put(security.getSymbol(), newPrice);
        return newPrice;
    }

    @Scheduled(fixedRate = 1000L, initialDelay = 0L)
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

        List<Security> stocks = securityService.findAllStocks();
        for (Security stock : stocks) {
            StockMarketDataMessage.StockMarketChange change1 = new StockMarketDataMessage.StockMarketChange();
            change1.setSymbol(stock.getSymbol());
            change1.setLatestPrice(BigDecimal.valueOf(getRandomPrice(stock)).setScale(2, RoundingMode.UP));
            stockMarketDataMessage.getChanges().add(change1);
        }
        return stockMarketDataMessage;
    }
}
