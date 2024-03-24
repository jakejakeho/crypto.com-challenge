package org.example.marketData.option;

import java.math.BigDecimal;

public class OptionMarketDataMessage {

    private String symbol;

    private BigDecimal latestPrice;

    public OptionMarketDataMessage(String symbol, BigDecimal optionPrice) {
        this.symbol = symbol;
        this.latestPrice = optionPrice;
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

    @Override
    public String toString() {
        return "OptionMarketDataMessage{" +
                "symbol='" + symbol + '\'' +
                ", latestPrice=" + latestPrice +
                '}';
    }
}
