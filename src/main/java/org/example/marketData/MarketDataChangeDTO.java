package org.example.marketData;

import java.math.BigDecimal;

public class MarketDataChangeDTO {

    private String symbol;
    private BigDecimal latestPrice;

    public MarketDataChangeDTO(String symbol, BigDecimal latestPrice) {
        this.symbol = symbol;
        this.latestPrice = latestPrice;
    }
}
