package com.example.p2p.resourceServer.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CheckDTO {

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
