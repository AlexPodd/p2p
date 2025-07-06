package com.example.p2p.resourceServer.util;

import com.example.p2p.resourceServer.exeption.InvalidCheckTypeException;
import com.example.p2p.resourceServer.model.currency.Currency;

public class CurrencyUtil {

    private final CurrencyProperties properties;

    public CurrencyUtil(CurrencyProperties properties) {
        this.properties = properties;
    }



    public Currency typeToCurrency(String type) throws Exception{
        if(properties.getCurrency(type) == null){
            throw new InvalidCheckTypeException();
        }

       return properties.getCurrency(type);
    }
}
