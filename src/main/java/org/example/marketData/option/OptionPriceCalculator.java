package org.example.marketData.option;

import org.example.security.Security;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
class OptionPriceCalculator {

    public BigDecimal getOptionPrice(Security symbol, BigDecimal stockPrice) {
        return BigDecimal.ONE;
    }
}
