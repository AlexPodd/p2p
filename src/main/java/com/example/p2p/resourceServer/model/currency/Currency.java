package com.example.p2p.resourceServer.model.currency;

import java.math.BigInteger;

public class Currency implements Cloneable {
    protected final String code;
    protected final int precision;
    protected BigInteger amount_integer, amount_fraction;

    public Currency(String code, int precision) {
        this.code = code;
        this.precision = precision;
    }

    @Override
    public Currency clone() {
        try {
            Currency clone = (Currency) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void setAmount_integer(BigInteger amount_integer) {
        this.amount_integer = amount_integer;
    }

    public void setAmount_fraction(BigInteger amount_fraction) {
        this.amount_fraction = amount_fraction;
    }

    public BigInteger getAmount_integer() {
        return amount_integer;
    }

    public BigInteger getAmount_fraction() {
        return amount_fraction;
    }

    public String getCode() {
        return code;
    }

    public int getPrecision() {
        return precision;
    }

    public void add(Currency other) {
        if (!this.code.equals(other.code) || this.precision != other.precision) {
            throw new IllegalArgumentException("Mismatched currency code or precision");
        }

        BigInteger base = BigInteger.TEN.pow(precision);

        BigInteger totalThis = this.amount_integer.multiply(base).add(this.amount_fraction);
        BigInteger totalOther = other.amount_integer.multiply(base).add(other.amount_fraction);
        BigInteger result = totalThis.add(totalOther);

        this.amount_integer = result.divide(base);
        this.amount_fraction = result.remainder(base);
    }

    public void subtract(Currency other) {
        if (!this.code.equals(other.code) || this.precision != other.precision) {
            throw new IllegalArgumentException("Mismatched currency code or precision");
        }

        BigInteger base = BigInteger.TEN.pow(precision);

        BigInteger totalThis = this.amount_integer.multiply(base).add(this.amount_fraction);
        BigInteger totalOther = other.amount_integer.multiply(base).add(other.amount_fraction);

        if (totalThis.compareTo(totalOther) < 0) {
            throw new IllegalArgumentException("Not enough funds to subtract");
        }

        BigInteger result = totalThis.subtract(totalOther);

        this.amount_integer = result.divide(base);
        this.amount_fraction = result.remainder(base);
    }
}
