package org.example.protfolio;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class PortfolioDTO {

    private Integer messageId;

    private List<PriceChangeDTO> priceChange;

    private List<SecurityDTO> securities = Collections.emptyList();

    private BigDecimal totalPortfolio;

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public List<PriceChangeDTO> getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(List<PriceChangeDTO> priceChange) {
        this.priceChange = priceChange;
    }

    public List<SecurityDTO> getSecurities() {
        return securities;
    }

    public void setSecurities(List<SecurityDTO> securities) {
        this.securities = securities;
    }

    public BigDecimal getTotalPortfolio() {
        return totalPortfolio;
    }

    public void setTotalPortfolio(BigDecimal totalPortfolio) {
        this.totalPortfolio = totalPortfolio;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n## ").append(messageId).append(" Market Data Update\n");
        for (PriceChangeDTO pc : getPriceChange())
            stringBuilder.append(pc);
        stringBuilder.append("\n\n## Portfolio\n");
        stringBuilder.append(String.format("%-20s%20s%20s%20s\n", "symbol", "price", "qty", "value"));
        for (SecurityDTO securityDTO : getSecurities()) {
            stringBuilder.append(securityDTO.toString());
            stringBuilder.append("\n");
        }
        stringBuilder.append('\n');
        stringBuilder.append('\n');
        stringBuilder.append(String.format("%-20s%60s", "#Total portfolio", getTotalPortfolio()));
        return stringBuilder.toString();
    }
}
