package org.example.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table
public class Security {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String symbol;

    String securityType;

    BigInteger strikePrice;

    Date maturityDate;
}
