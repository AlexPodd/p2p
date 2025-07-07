package com.example.p2p.resourceServer.controller;


import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("bankTransfer")
@Validated
@Tag(name = "P2P API", description = "P2P переводы между счетами")
public class P2PController {

    private final TransferService transferService;

    public P2PController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Operation(
            summary = "Создать перевод",
            description = "Создает заявку на перевод между счетами. Не завершает перевод, только инициирует транзакцию."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заявка успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные или недостаточно средств", content = @Content),
            @ApiResponse(responseCode = "403", description = "Нет доступа к счету", content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
    })
    @PostMapping("")
    public ResponseEntity<?> initiate(@RequestBody @Valid TransferDTO transferInfo) throws Exception {
        long transactionId = transferService.initiate(transferInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Transfer is prepared. Please confirm.",
                "transactionId", transactionId
        ));
    }

    @Operation(summary = "Отменить перевод", description = "Удаляет перевод, если он не был подтвержден")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Перевод успешно отменен"),
            @ApiResponse(responseCode = "403", description = "Нет доступа к переводу", content = @Content),
            @ApiResponse(responseCode = "404", description = "Перевод не найден", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancel(@PathVariable @Min(1) long id) throws Exception {
        transferService.cancelTransfer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                "message", "Transfer is cancel."
        ));
    }

    @Operation(summary = "Подтвердить перевод", description = "Подтверждает и завершает перевод, списывая и зачисляя средства")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод успешно выполнен"),
            @ApiResponse(responseCode = "403", description = "Нет доступа к заявке", content = @Content),
            @ApiResponse(responseCode = "404", description = "Заявка не найден", content = @Content),
            @ApiResponse(responseCode = "410", description = "Заявка истек (неподтвержден более 10 минут)", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> confirm(@PathVariable @Min(1) long id) throws Exception {
        transferService.transfer(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message", "Transfer is do."
        ));
    }



}
