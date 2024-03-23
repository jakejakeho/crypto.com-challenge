package org.example;

import java.math.BigDecimal;

public class Position {

    private String symbol;

    private BigDecimal positionSize;

    public Position(String symbol, String positionSize) {
        this.symbol = symbol;
        this.positionSize = new BigDecimal(positionSize);
    }

}
