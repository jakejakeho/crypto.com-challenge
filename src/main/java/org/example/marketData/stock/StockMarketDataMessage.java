package org.example.marketData.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StockMarketDataMessage {

    private Integer messageId;

    private List<StockMarketChange> changes = new ArrayList<>();

    public static class StockMarketChange{
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

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public List<StockMarketChange> getChanges() {
        return changes;
    }

    public void setChanges(List<StockMarketChange> changes) {
        this.changes = changes;
    }
}
