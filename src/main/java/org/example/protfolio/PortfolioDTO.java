package org.example.protfolio;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class PortfolioDTO {

    private Integer id;

    private PriceChangeDTO priceChange;

    private List<SecurityDTO> securities = Collections.emptyList();

    private BigDecimal totalPortfolio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PriceChangeDTO getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(PriceChangeDTO priceChange) {
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
        stringBuilder.append("## ").append(id).append(" Market Data Update\n");
        stringBuilder.append(getPriceChange());
        stringBuilder.append("\n\n## Portfolio\n");
        stringBuilder.append("symbol\t\t\tprice\t\t\tqty\t\t\tvalue\t\t\t");
        for (SecurityDTO securityDTO : getSecurities()) {
            stringBuilder.append(securityDTO.toString());
            stringBuilder.append("\n");
        }
        stringBuilder.append('\n');
        stringBuilder.append("#Total portfolio \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t").append(getTotalPortfolio());
        return stringBuilder.toString();
    }
}
