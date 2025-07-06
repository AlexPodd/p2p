package com.example.p2p.resourceServer.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
public class CheckDTO {

    public CheckDTO(String currencyType, String balance) {
        this.currencyType = currencyType;
        this.balance = balance;
    }

    public CheckDTO(){

    }
    @NotBlank
    private String currencyType;

    @NotBlank
    @Pattern(regexp = "^\\d+\\.\\d+$")
    private String balance;

    public @NotBlank String getCurrencyType() {
        return currencyType;
    }

    public @NotBlank @Pattern(regexp = "^\\d+\\.\\d+$") String getBalance() {
        return balance;
    }


}
