package com.example.p2p.resourceServer.controller;

import com.example.p2p.authServer.dto.UserDTO;
import com.example.p2p.authServer.service.UserService;
import com.example.p2p.resourceServer.dto.BalanceResponse;
import com.example.p2p.resourceServer.dto.CheckDTO;
import com.example.p2p.resourceServer.dto.IdRequest;
import com.example.p2p.resourceServer.service.CheckService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/checks")
@Validated
public class CheckController {

    private final CheckService checkService;

    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @PostMapping()
    public ResponseEntity<?> createCheck(@RequestBody @Valid CheckDTO check) throws Exception {
        checkService.createCheck(check);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("{\"message\":\"Check created successfully\"}");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> closeCheck(@PathVariable @Min(1) long id) throws Exception {
        checkService.closeCheck(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("{\"message\":\"Check close successfully\"}");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable @Min(1) long id) throws Exception {
        String balance = checkService.getBalance(id);
        return ResponseEntity.ok()
                .body(new BalanceResponse(balance, "Balance retrieved successfully"));
    }

    @GetMapping()
    public ResponseEntity<List<Long>> getAllCheck() throws Exception {
        return ResponseEntity.ok(checkService.getAllCheck());
    }
}
