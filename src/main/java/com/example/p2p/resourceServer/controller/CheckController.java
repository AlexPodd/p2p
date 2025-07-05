package com.example.p2p.resourceServer.controller;

import com.example.p2p.authServer.dto.UserDTO;
import com.example.p2p.authServer.service.UserService;
import com.example.p2p.resourceServer.dto.BalanceResponse;
import com.example.p2p.resourceServer.dto.CheckDTO;
import com.example.p2p.resourceServer.dto.IdRequest;
import com.example.p2p.resourceServer.service.CheckService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/check")
public class CheckController {
//open (s summoi) close watch perevod

    private final CheckService checkService;

    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCheck(@RequestBody @Valid CheckDTO check) throws Exception {
        checkService.createCheck(check);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("{\"message\":\"Check created successfully\"}");
    }

    @PostMapping("/close")
    public ResponseEntity<?> closeCheck(@RequestBody IdRequest id) throws Exception {
        checkService.closeCheck(id.getId());
        return ResponseEntity.status(HttpStatus.GONE)
                .body("{\"message\":\"Check close successfully\"}");
    }

    @GetMapping("/get")
    public ResponseEntity<BalanceResponse> getBalance(@RequestParam  long id) throws Exception {
        String balance = checkService.getBalance(id);
        return ResponseEntity.ok()
                .body(new BalanceResponse(balance, "Balance retrieved successfully"));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Long>> getAllCheck() throws Exception {
        return ResponseEntity.ok(checkService.getAllCheck());
    }
}
