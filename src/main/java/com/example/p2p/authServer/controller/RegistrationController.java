package com.example.p2p.authServer.controller;

import com.example.p2p.authServer.dto.UserDTO;
import com.example.p2p.authServer.model.User;
import com.example.p2p.authServer.repository.UserRepository;
import com.example.p2p.authServer.service.CustomUserDetails;
import com.example.p2p.authServer.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService service) {
        this.userService = service;
    }

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт нового пользователя в системе, если такой логин ещё не занят"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким логином уже существует"),
            @ApiResponse(responseCode = "400", description = "Невалидные входные данные"),
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {
        boolean success = userService.register(dto);
        if (!success) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{\"error\":\"User already exists\"}");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("{\"message\":\"User registered successfully\"}");
    }
}
