package ru.project.jbd.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.project.jbd.security.payload.request.LoginRequest;
import ru.project.jbd.security.payload.request.SignupRequest;
import ru.project.jbd.security.services.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Контроллер аутентификации")
public class AuthController {
    
    private final AuthService service;

    @PostMapping("/signin")
    @Operation(summary = "Авторизация и получение cookie")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return service.authentication(loginRequest);
    }

    @PostMapping("/signup")
    @Operation(summary = "Регистрация")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return service.registration(signUpRequest);
    }

    @PostMapping("/signout")
    @Operation(summary = "Выход и обнуление cookie")
    public ResponseEntity<?> logoutUser() {
        return service.logout();
    }

    @PostMapping("/refreshtoken")
    @Operation(summary = "Обновление токена")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
        return service.refreshToken(request);
    }
}

