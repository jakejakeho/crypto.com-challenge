package org.example;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PortfolioResultSubscriber {

    final PositionReader positionReader;

    public PortfolioResultSubscriber(PositionReader positionReader) {
        this.positionReader = positionReader;
    }

    @Scheduled(fixedRate = 1000L)
    public void print() {
        System.out.println("haha");
    }
}
