package com.example.p2p.resourceServer.service;

import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.exeption.NoAccessToCheckException;
import com.example.p2p.resourceServer.exeption.transferException.ExpireTransactionException;
import com.example.p2p.resourceServer.model.Check;
import com.example.p2p.resourceServer.model.Transfer;
import com.example.p2p.resourceServer.repository.CheckRepository;
import com.example.p2p.resourceServer.repository.TransferRepository;
import com.example.p2p.resourceServer.util.CurrencyUtil;
import com.example.p2p.resourceServer.util.UserContext;
import com.example.p2p.resourceServer.util.transferValidatorChain.EnoughValueValid;
import com.example.p2p.resourceServer.util.transferValidatorChain.TransferValidator;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommonTransferService implements TransferService{


    private final List<TransferValidator> validators;

    private final CheckRepository checkRepository;
    private final TransferRepository transferRepository;
    private final CurrencyUtil currencyUtil;
    private final UserContext context;
    private final EnoughValueValid enoughValueValid;

    public CommonTransferService(List<TransferValidator> validators, CurrencyUtil currencyUtil, UserContext context, TransferRepository transferRepository, CheckRepository checkRepository, EnoughValueValid enoughValueValid) {
        this.validators= validators;
        this.currencyUtil = currencyUtil;
        this.context = context;
        this.transferRepository = transferRepository;
        this.checkRepository = checkRepository;
        this.enoughValueValid = enoughValueValid;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(long transferID) throws Exception {
        Transfer transfer = (Transfer) transferRepository.getTransfer(transferID, new Transfer());

        if(!checkRepository.getNameByID(transfer.getCheckFromId()).equals(context.getUserName())){
            throw new NoAccessToCheckException();
        }

        Duration duration = Duration.between(transfer.getCreatedAt(), LocalDateTime.now());
        if (duration.toMinutes() > 10) {
            throw new ExpireTransactionException();
        }

        String sum = transfer.getCurrency().getAmount_integer()+"."+transfer.getCurrency().getAmount_fraction();
        enoughValueValid.validate(new TransferDTO(transfer.getCheckFromId(), transfer.getCheckToId(), sum),checkRepository);
        Check checkFrom = (Check) checkRepository.getCheckByID(transfer.getCheckFromId(), new Check());
        Check checkTo = (Check) checkRepository.getCheckByID(transfer.getCheckToId(), new Check());

        checkFrom.getCurrency().subtract(transfer.getCurrency());
        checkTo.getCurrency().add(transfer.getCurrency());

        transferRepository.deleteTransfer(transferID);
        checkRepository.updateCheck(checkFrom);
        checkRepository.updateCheck(checkTo);
    }

    @Override
    public void cancelTransfer(long transferID) {
        long checkIDFrom = transferRepository.getTransferFromId(transferID);

        if(!checkRepository.getNameByID(checkIDFrom).equals(context.getUserName())){
            throw new NoAccessToCheckException();
        }

        transferRepository.deleteTransfer(transferID);
    }

    @Override
    public long initiate(TransferDTO transferDTO) throws Exception {
        for(TransferValidator validator: validators){
            validator.validate(transferDTO, checkRepository);
        }
        long id = new SecureRandom().nextLong() & Long.MAX_VALUE;

        String[] sum = transferDTO.getSum().split("\\.");
        BigInteger integer = new BigInteger(sum[0]);
        BigInteger fracture = new BigInteger(sum[1]);

        String type = checkRepository.getTypeByID(transferDTO.getIdFrom());
        transferRepository.createTransfer(id, transferDTO, integer, fracture, type);
        return id;
    }
}
