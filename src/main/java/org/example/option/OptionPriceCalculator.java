package org.example.option;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
class OptionPriceCalculator {

    public BigDecimal getOptionPrice(String symbol, BigDecimal stockPrice) {
        return BigDecimal.ONE;
    }

}
