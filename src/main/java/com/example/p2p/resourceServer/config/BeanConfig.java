package com.example.p2p.resourceServer.config;


import com.example.p2p.resourceServer.util.CurrencyProperties;
import com.example.p2p.resourceServer.util.CurrencyUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    @ConfigurationProperties("currencies")
    public CurrencyProperties currencyProperties(){
        return new CurrencyProperties();
    }

    @Bean
    public CurrencyUtil currencyUtil(CurrencyProperties currencyProperties) {
        return new CurrencyUtil(currencyProperties);
    }


}
