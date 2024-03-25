package org.example.protfolio;

import org.example.marketData.MarketDataChangeDTO;
import org.example.marketData.MarketDataProvider;
import org.example.marketData.MarketDataUpdateMessageDTO;
import org.example.position.Position;
import org.example.position.PositionReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class PortfolioProvider {

    public PortfolioProvider(MarketDataProvider marketDataProvider, PositionReader positionReader) {
        this.marketDataProvider = marketDataProvider;
        this.positionReader = positionReader;
    }

    private final MarketDataProvider marketDataProvider;

    private final PositionReader positionReader;

    private final Logger log = LoggerFactory.getLogger(PortfolioProvider.class);

    private List<Consumer<PortfolioDTO>> consumers = Collections.synchronizedList(new ArrayList<>());

    private ExecutorService publishThreadPool = Executors.newSingleThreadExecutor();

    private final Map<String, BigDecimal> priceMap = new HashMap<>();

    public void subscribe(Consumer<PortfolioDTO> consumer) {
        consumers.add(consumer);
    }

    @PostConstruct
    private void subscribePrice() {
        marketDataProvider.subscribe(this::processMarketData);
    }

    private void processMarketData(MarketDataUpdateMessageDTO marketDataUpdateMessageDTO) {
        updatePriceMap(marketDataUpdateMessageDTO);
        publishPortfolio(calculatePortfolio(marketDataUpdateMessageDTO));
    }

    private PortfolioDTO calculatePortfolio(MarketDataUpdateMessageDTO marketDataUpdateMessageDTO) {
        List<Position> positions = positionReader.readCsvFile();

        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setMessageId(marketDataUpdateMessageDTO.getMessageId());
        portfolioDTO.setPriceChange(getPriceChanges(marketDataUpdateMessageDTO, positions));
        setSecurities(portfolioDTO, positions);
        return portfolioDTO;
    }

    private List<PriceChangeDTO> getPriceChanges(MarketDataUpdateMessageDTO marketDataUpdateMessageDTO, List<Position> positions) {
        List<PriceChangeDTO> priceChangeDTOS = new ArrayList<>();
        Map<String, MarketDataChangeDTO> marketDataChangeDTOMap = new HashMap<>();
        for (MarketDataChangeDTO changeDTO : marketDataUpdateMessageDTO.getChanges()) {
            marketDataChangeDTOMap.put(changeDTO.getSymbol(), changeDTO);
        }

        for (Position position : positions) {
            if (marketDataChangeDTOMap.containsKey(position.getSymbol())) {
                MarketDataChangeDTO marketDataChangeDTO = marketDataChangeDTOMap.get(position.getSymbol());
                priceChangeDTOS.add(new PriceChangeDTO(marketDataChangeDTO.getSymbol(), marketDataChangeDTO.getLatestPrice()));
            }
        }

        return priceChangeDTOS;
    }

    private void setSecurities(PortfolioDTO portfolioDTO, List<Position> positions) {
        BigDecimal totalPortfolio = BigDecimal.ZERO;

        List<SecurityDTO> securityDTOS = new ArrayList<>();
        for (Position position : positions) {
            SecurityDTO securityDTO = new SecurityDTO();
            securityDTO.setSymbol(position.getSymbol());
            securityDTO.setPrice(priceMap.getOrDefault(position.getSymbol(), BigDecimal.ZERO));
            securityDTO.setQty(position.getPositionSize());
            BigDecimal value = securityDTO.getPrice().multiply(securityDTO.getQty());
            securityDTO.setValue(value);
            totalPortfolio = totalPortfolio.add(value);
            securityDTOS.add(securityDTO);
        }

        portfolioDTO.setSecurities(securityDTOS);
        portfolioDTO.setTotalPortfolio(totalPortfolio);
    }

    private void updatePriceMap(MarketDataUpdateMessageDTO marketDataUpdateMessageDTO) {
        for (MarketDataChangeDTO change : marketDataUpdateMessageDTO.getChanges()) {
            priceMap.put(change.getSymbol(), change.getLatestPrice());
        }
    }

    private void publishPortfolio(PortfolioDTO portfolioDTO) {
        for (Consumer<PortfolioDTO> consumer : consumers) {
            CompletableFuture.runAsync(() -> consumer.accept(portfolioDTO), publishThreadPool);
        }
    }
}
