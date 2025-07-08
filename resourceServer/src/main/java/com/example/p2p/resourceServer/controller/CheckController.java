package com.example.p2p.resourceServer.controller;

import com.example.p2p.resourceServer.dto.BalanceResponse;
import com.example.p2p.resourceServer.dto.CheckDTO;
import com.example.p2p.resourceServer.service.CheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checks")
@Validated
@Tag(name = "Check API", description = "Управление счетами")
public class CheckController {

    private final CheckService checkService;

    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }


    @Operation(summary = "Создать новый счёт")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Счёт успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные (checkDTO)")
    })
    @PostMapping()
    public ResponseEntity<?> createCheck(@RequestBody @Valid CheckDTO check) throws Exception {
        checkService.createCheck(check);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("{\"message\":\"Check created successfully\"}");
    }

    @Operation(summary = "Закрыть счёт")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Счёт успешно закрыт"),
            @ApiResponse(responseCode = "403", description = "Нет доступа к счёту"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID счёта")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> closeCheck(@PathVariable @Min(1) long id) throws Exception {
        checkService.closeCheck(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("{\"message\":\"Check close successfully\"}");
    }

    @Operation(summary = "Получить баланс по счёту")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Баланс успешно получен"),
            @ApiResponse(responseCode = "403", description = "Нет доступа к счёту"),
            @ApiResponse(responseCode = "404", description = "Счёт не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID счёта")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable @Min(1) long id) throws Exception {
        String balance = checkService.getBalance(id);
        return ResponseEntity.ok()
                .body(new BalanceResponse(balance, "Balance retrieved successfully"));
    }

    @Operation(summary = "Получить список всех ID счетов (для пользователя)")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping()
    public ResponseEntity<List<Long>> getAllCheck() throws Exception {
        return ResponseEntity.ok(checkService.getAllCheck());
    }
}
