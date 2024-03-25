package org.example.marketData.option;

import java.math.BigDecimal;
import java.util.List;

public class OptionMarketDataMessage {

    private Integer messageId;

    private List<OptionMarketDataChange> changes;

    public static class OptionMarketDataChange {

        public OptionMarketDataChange(String symbol, BigDecimal latestPrice) {
            this.symbol = symbol;
            this.latestPrice = latestPrice;
        }

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

    public List<OptionMarketDataChange> getChanges() {
        return changes;
    }

    public void setChanges(List<OptionMarketDataChange> changes) {
        this.changes = changes;
    }
}
