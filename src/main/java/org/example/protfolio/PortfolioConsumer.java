package org.example.protfolio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class PortfolioConsumer {

    private final PortfolioProvider portfolioProvider;

    public PortfolioConsumer(PortfolioProvider portfolioProvider) {
        this.portfolioProvider = portfolioProvider;
    }

    private final Logger log = LoggerFactory.getLogger(PortfolioConsumer.class);

    private final ExecutorService consumerThreadPool = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void subscribe() {
        portfolioProvider.subscribe(this:: processPortfolio);
    }

    private void processPortfolio(PortfolioDTO portfolioDTO) {
        CompletableFuture.runAsync(() -> log.info(portfolioDTO.toString()), consumerThreadPool);
    }
}
