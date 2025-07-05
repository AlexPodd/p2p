package com.example.p2p.resourceServer.util;

import com.example.p2p.resourceServer.model.currency.Currency;

public class CurrencyUtil {

    private final CurrencyProperties properties;

    public CurrencyUtil(CurrencyProperties properties) {
        this.properties = properties;
    }



    public Currency typeToCurrency(String type){
       return properties.getCurrency(type);
    }
}
