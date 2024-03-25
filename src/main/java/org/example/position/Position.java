package org.example.position;

import java.math.BigDecimal;

public class Position {

    private String symbol;

    private BigDecimal positionSize;

    public Position(String symbol, String positionSize) {
        this.symbol = symbol;
        this.positionSize = new BigDecimal(positionSize);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPositionSize() {
        return positionSize;
    }

    public void setPositionSize(BigDecimal positionSize) {
        this.positionSize = positionSize;
    }
}
