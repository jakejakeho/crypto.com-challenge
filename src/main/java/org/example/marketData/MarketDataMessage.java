package org.example.marketData;

import java.math.BigDecimal;

public class MarketDataMessage {

    private String symbol;

    private BigDecimal latestPrice;

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

    @Override
    public String toString() {
        return "MarketDataMessage{" +
                "symbol='" + symbol + '\'' +
                ", latestPrice=" + latestPrice +
                '}';
    }
}
