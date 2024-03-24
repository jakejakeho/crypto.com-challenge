package org.example.protfolio;

import java.math.BigDecimal;

public class SecurityDTO {
    public String symbol;

    public BigDecimal price;

    public BigDecimal qty;

    public BigDecimal value;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getSymbol() + "\t\t\t" + getPrice() + "\t\t\t" + getQty() + "\t\t\t" + getValue();
    }
}
