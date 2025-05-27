package ecommerce.middleware.controller;

import ecommerce.middleware.auth.dto.LoginRequestDTO;
import ecommerce.middleware.auth.dto.RegisterRequestDTO;
import ecommerce.middleware.auth.dto.LoginResponseDTO;
import ecommerce.middleware.service.AuthService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackAuth")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        authService.register(request);
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackLogin")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    public ResponseEntity<String> fallbackAuth(RegisterRequestDTO request, Throwable t) {
    return ResponseEntity.status(503).body("Registro temporariamente indisponível");
}

    public ResponseEntity<LoginResponseDTO> fallbackLogin(LoginRequestDTO request, Throwable t) {
    return ResponseEntity.status(503).body(null);
    }
}

