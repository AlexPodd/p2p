package com.example.p2p.resourceServer.model;

import com.example.p2p.resourceServer.model.currency.Currency;
import com.example.p2p.resourceServer.model.currency.CurrencyType;

public abstract class CheckAbstract {
    protected long id;
    protected long userID;
    protected CurrencyType type;
    protected Currency currency;

    public CheckAbstract(){
    }
    public CheckAbstract(long id, long userID, CurrencyType type, Currency currency) {
        this.id = id;
        this.userID = userID;
        this.type = type;
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public CurrencyType getType() {
        return type;
    }

    public void setType(CurrencyType type) {
        this.type = type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
