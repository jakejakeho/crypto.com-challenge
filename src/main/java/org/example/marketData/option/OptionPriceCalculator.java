package org.example.marketData.option;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.example.security.Security;
import org.example.security.SecurityType;
import org.springframework.stereotype.Service;

@Service
class OptionPriceCalculator {

    private static final double RISK_FREE_RATE = 0.02;

    public BigDecimal getOptionPrice(Security security, BigDecimal stockPrice) {
        return BigDecimal.valueOf(getOptionPriceDouble(security, stockPrice));
    }

    private double getOptionPriceDouble(Security security, BigDecimal stockPriceBigDecimal) {
        double stockPrice = stockPriceBigDecimal.doubleValue();
        double strikePrice = security.getStrikePrice()
                                     .doubleValue();
        double timeToExpiration = getYearsBetween(new Date(), security.getMaturityDate());
        double volatility = 0.2;

        double d1 = (Math.log(stockPrice / strikePrice) + (RISK_FREE_RATE + 0.5 * Math.pow(volatility, 2))
            * timeToExpiration) / (volatility * Math.sqrt(timeToExpiration));
        double d2 = d1 - volatility * Math.sqrt(timeToExpiration);

        NormalDistribution normalDistribution = new NormalDistribution();
        if (Objects.equals(security.getSecurityType(), SecurityType.CALL.toString())) {
            return stockPrice * normalDistribution.cumulativeProbability(d1) - strikePrice * Math.exp(
                -RISK_FREE_RATE * timeToExpiration) * normalDistribution.cumulativeProbability(d2);
        } else if (Objects.equals(security.getSecurityType(), SecurityType.PUT.toString())) {
            return strikePrice * Math.exp(-RISK_FREE_RATE * timeToExpiration)
                * normalDistribution.cumulativeProbability(-d2) - stockPrice * normalDistribution.cumulativeProbability(
                -d1);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static final double YEAR_IN_MILLS = 365.25 * 24 * 60 * 60 * 1000;

    private double getYearsBetween(Date d1, Date d2) {
        long durationMills = d2.getTime() - d1.getTime();
        return durationMills / YEAR_IN_MILLS;
    }

    private double getVolatility() {
        /// TODO: get stock price's annualized standard deviation
        return 0.02;
    }
}
