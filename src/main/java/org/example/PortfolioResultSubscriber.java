package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PortfolioResultSubscriber {

    Logger log = LoggerFactory.getLogger(PortfolioResultSubscriber.class);

    final PositionReader positionReader;

    public PortfolioResultSubscriber(PositionReader positionReader) {
        this.positionReader = positionReader;
    }

    @Scheduled(fixedRate = 1000L)
    public void print() {
        log.info("haha");
    }
}
