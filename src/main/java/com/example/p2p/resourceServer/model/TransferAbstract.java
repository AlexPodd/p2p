package com.example.p2p.resourceServer.model;

import com.example.p2p.resourceServer.model.currency.Currency;

import java.time.LocalDateTime;

public abstract class TransferAbstract {
    protected long id;
    protected long checkFromId;
    protected long checkToId;
    protected Currency currency;
    protected LocalDateTime createdAt;
    public TransferAbstract(){}

    public TransferAbstract(long id, long checkFromId, long checkToId, Currency currency, LocalDateTime createdAt) {
        this.id = id;
        this.checkFromId = checkFromId;
        this.checkToId = checkToId;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCheckFromId() {
        return checkFromId;
    }

    public void setCheckFromId(long checkFromId) {
        this.checkFromId = checkFromId;
    }

    public long getCheckToId() {
        return checkToId;
    }

    public void setCheckToId(long checkToId) {
        this.checkToId = checkToId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
