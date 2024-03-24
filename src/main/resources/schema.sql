CREATE TABLE IF NOT EXISTS SECURITY
(
    ID            INTEGER auto_increment,
    SYMBOL        CHARACTER VARYING,
    SECURITY_TYPE CHARACTER VARYING,
    STRIKE_PRICE  BIGINT,
    MATURITY_DATE DATE,

    PRIMARY KEY (ID)
);

comment on column SECURITY.SECURITY_TYPE is 'STOCK, CALL, PUT';

