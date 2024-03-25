package org.example.security;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table
public class Security {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String symbol;

    private String securityType;

    private BigInteger strikePrice;

    private Date maturityDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public BigInteger getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(BigInteger strikePrice) {
        this.strikePrice = strikePrice;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    @Override
    public String toString() {
        return "Security{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", securityType='" + securityType + '\'' +
                ", strikePrice=" + strikePrice +
                ", maturityDate=" + maturityDate +
                '}';
    }
}
