package org.example.option;

import java.math.BigDecimal;

public class OptionMarketDataMessage {

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
        return "OptionMarketDataMessage{" +
                "symbol='" + symbol + '\'' +
                ", latestPrice=" + latestPrice +
                '}';
    }
}
