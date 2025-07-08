package com.example.p2p.resourceServer.model;

import com.example.p2p.resourceServer.model.currency.Currency;
import com.example.p2p.resourceServer.model.currency.CurrencyType;

public abstract class CheckAbstract {
    protected long id;
    protected String userID;
    protected CurrencyType type;
    protected Currency currency;

    public CheckAbstract(){
    }
    public CheckAbstract(long id, String userID, CurrencyType type, Currency currency) {
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
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
