package org.example.protfolio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class PortfolioResultSubscriber {

    private final PortfolioProvider portfolioProvider;

    public PortfolioResultSubscriber(PortfolioProvider portfolioProvider) {
        this.portfolioProvider = portfolioProvider;
    }

    private final Logger log = LoggerFactory.getLogger(PortfolioResultSubscriber.class);

    private final ExecutorService consumerThreadPool = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void subscribe() {
        portfolioProvider.subscribe(getConsumer());
    }

    private Consumer<PortfolioDTO> getConsumer() {
        return portfolioDTO -> CompletableFuture.runAsync(() -> {
            log.info(portfolioDTO.toString());
        }, consumerThreadPool);
    }
}
