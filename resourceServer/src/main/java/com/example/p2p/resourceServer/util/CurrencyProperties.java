package com.example.p2p.resourceServer.util;

import com.example.p2p.resourceServer.model.currency.Currency;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@ConfigurationProperties("currencies")
public class CurrencyProperties {
    private Map<String, Currency> currencies = new HashMap<>();


    public void setCurrencies(Map<String, Currency> currencies) {
        this.currencies = currencies;
    }

    public Currency getCurrency(String key) {
        return currencies.get(key).clone();
    }
}
