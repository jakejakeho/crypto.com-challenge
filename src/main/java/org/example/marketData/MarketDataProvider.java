package org.example.marketData;


import org.example.marketData.option.OptionMarketDataMessage;
import org.example.marketData.option.OptionMarketDataProvider;
import org.example.marketData.stock.StockMarketDataMessage;
import org.example.marketData.stock.StockMarketDataProvider;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class MarketDataProvider {

    public MarketDataProvider(StockMarketDataProvider stockMarketDataProvider, OptionMarketDataProvider optionMarketDataProvider) {
        this.stockMarketDataProvider = stockMarketDataProvider;
        this.optionMarketDataProvider = optionMarketDataProvider;
    }

    private final StockMarketDataProvider stockMarketDataProvider;

    private final OptionMarketDataProvider optionMarketDataProvider;

    private final Map<Integer, List<MarketDataChangeDTO>> map = Collections.synchronizedMap(new HashMap<>());

    private final List<Consumer<MarketDataUpdateMessageDTO>> consumers = new ArrayList<>();

    private final ExecutorService publishThreadPool = Executors.newSingleThreadExecutor();

    public void subscribe(Consumer<MarketDataUpdateMessageDTO> consumer) {
        consumers.add(consumer);
    }

    @PostConstruct
    private void subscribePrice() {
        stockMarketDataProvider.subscribe(this::processStockPrice);
        optionMarketDataProvider.subscribe(this::processOptionPrice);
    }

    private void processStockPrice(StockMarketDataMessage stockMarketDataMessage) {
        map.putIfAbsent(stockMarketDataMessage.getMessageId(), Collections.synchronizedList(new ArrayList<>()));
        for (StockMarketDataMessage.StockMarketChange change : stockMarketDataMessage.getChanges()) {
            map.get(stockMarketDataMessage.getMessageId()).add(new MarketDataChangeDTO(change.getSymbol(),
                change.getLatestPrice()));
        }
    }

    private void processOptionPrice(OptionMarketDataMessage optionMarketDataMessage) {
        map.putIfAbsent(optionMarketDataMessage.getMessageId(), Collections.synchronizedList(new ArrayList<>()));
        for (OptionMarketDataMessage.OptionMarketDataChange change : optionMarketDataMessage.getChanges()) {
            map.get(optionMarketDataMessage.getMessageId()).add(new MarketDataChangeDTO(change.getSymbol(), change.getLatestPrice()));
        }
        publish(optionMarketDataMessage.getMessageId());
    }

    private void publish(Integer messageId) {
        MarketDataUpdateMessageDTO marketDataUpdateMessageDTO = new MarketDataUpdateMessageDTO();
        marketDataUpdateMessageDTO.setMessageId(messageId);
        marketDataUpdateMessageDTO.setChanges(map.get(messageId));
        for (Consumer<MarketDataUpdateMessageDTO> consumer : consumers) {
            CompletableFuture.runAsync(() -> consumer.accept(marketDataUpdateMessageDTO), publishThreadPool);
        }
    }
}
