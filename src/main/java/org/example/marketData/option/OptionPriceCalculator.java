package org.example.marketData.option;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.example.security.Security;
import org.example.security.SecurityType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;

import static org.example.marketData.Constants.RISK_FREE_RATE;
import static org.example.marketData.Constants.VOLATILITY;

class OptionPriceCalculator {

    public BigDecimal getOptionPrice(Security security, BigDecimal stockPrice) {
        return BigDecimal.valueOf(getOptionPriceDouble(security, stockPrice)).setScale(2, RoundingMode.UP);
    }

    private double getOptionPriceDouble(Security security, BigDecimal stockPriceBigDecimal) {
        double stockPrice = stockPriceBigDecimal.doubleValue();
        double strikePrice = security.getStrikePrice()
                                     .doubleValue();
        double timeToExpiration = getYearsBetween(new Date(), security.getMaturityDate());


        double d1 = (Math.log(stockPrice / strikePrice) + (RISK_FREE_RATE + 0.5 * Math.pow(VOLATILITY, 2))
            * timeToExpiration) / (VOLATILITY * Math.sqrt(timeToExpiration));
        double d2 = d1 - VOLATILITY * Math.sqrt(timeToExpiration);

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
