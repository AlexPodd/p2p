package com.example.p2p.resourceServer.config;


import com.example.p2p.resourceServer.exeption.transferException.InvalidCurrencyTypeToTransferException;
import com.example.p2p.resourceServer.util.CurrencyProperties;
import com.example.p2p.resourceServer.util.CurrencyUtil;
import com.example.p2p.resourceServer.util.UserContext;
import com.example.p2p.resourceServer.util.transferValidatorChain.CheckAccessValid;
import com.example.p2p.resourceServer.util.transferValidatorChain.CheckCurrencyTypeValid;
import com.example.p2p.resourceServer.util.transferValidatorChain.CheckIsExistValid;
import com.example.p2p.resourceServer.util.transferValidatorChain.EnoughValueValid;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

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

    @Bean
    public UserContext userContext(){
        return new UserContext();
    }

    @Order(3)
    @Bean
    public EnoughValueValid enoughValueValid() {
        return new EnoughValueValid();
    }

    @Order(2)
    @Bean
    public CheckAccessValid accessValid(){
        return new CheckAccessValid();
    }

    @Order(1)
    @Bean
    public CheckIsExistValid checkIsExistValid(){
        return new CheckIsExistValid();
    }

    @Order(4)
    @Bean
    public CheckCurrencyTypeValid checkCurrencyTypeValid(){
        return new CheckCurrencyTypeValid();
    }
}
