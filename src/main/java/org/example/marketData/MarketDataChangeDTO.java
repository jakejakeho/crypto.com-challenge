package org.example.marketData;

import java.math.BigDecimal;

public class MarketDataChangeDTO {

    private String symbol;
    private BigDecimal latestPrice;

    public MarketDataChangeDTO(String symbol, BigDecimal latestPrice) {
        this.symbol = symbol;
        this.latestPrice = latestPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(BigDecimal latestPrice) {
        this.latestPrice = latestPrice;
    }
}
