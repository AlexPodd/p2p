package com.example.p2p.resourceServer.dto;

import jakarta.validation.constraints.*;

import java.math.BigInteger;

public class TransferDTO {

    @NotNull
    @Min(1)
    private long idFrom;

    @NotNull
    @Min(1)
    private long idTo;

    @NotBlank
    @Pattern(regexp = "^\\d+\\.\\d+$")
    private String sum;

    @AssertTrue(message = "sum must be greater than 0")
    public boolean isSumGreaterThanZero() {
        try {
            String[] sumWord = sum.split("\\.");
            BigInteger integer = new BigInteger(sumWord[0]);
            BigInteger fracture = new BigInteger(sumWord[1]);
            return integer.compareTo(BigInteger.ZERO) > 0
                    || (integer.compareTo(BigInteger.ZERO) == 0 && fracture.compareTo(BigInteger.ZERO) > 0);
        } catch (Exception e) {
            return false;
        }
    }

    public TransferDTO(long idFrom, long idTo, String sum) {
            this.idFrom = idFrom;
            this.idTo = idTo;
            this.sum = sum;
        }

        public TransferDTO() {

        }

    @NotNull
    @Min(1)
    public long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(@NotNull @Min(1) long idFrom) {
        this.idFrom = idFrom;
    }

    @NotNull
    @Min(1)
    public long getIdTo() {
        return idTo;
    }

    public void setIdTo(@NotNull @Min(1) long idTo) {
        this.idTo = idTo;
    }

    public @NotBlank @Pattern(regexp = "^\\d+\\.\\d+$") String getSum() {
        return sum;
    }

    public void setSum(@NotBlank @Pattern(regexp = "^\\d+\\.\\d+$") String sum) {
        this.sum = sum;
    }
}