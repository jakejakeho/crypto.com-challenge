package org.example.protfolio;

import java.math.BigDecimal;

public class PriceChangeDTO {

    public String symbol;

    public BigDecimal newPrice;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    @Override
    public String toString() {
        return getSymbol() + " change to " + newPrice.toString();
    }
}
