package org.example.marketData.stock;

import org.hibernate.collection.internal.PersistentList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StockMarketDataMessage {

    private Integer messageId;

    private List<StockMarketChange> changes = new ArrayList<>();

    private static class StockMarketChange{
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
    }
}
