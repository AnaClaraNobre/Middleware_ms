package ecommerce.middleware.controller;

import ecommerce.middleware.auth.dto.LoginRequestDTO;
import ecommerce.middleware.auth.dto.RegisterRequestDTO;
import ecommerce.middleware.auth.dto.LoginResponseDTO;
import ecommerce.middleware.service.AuthService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackAuth")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        logger.info("Recebida solicitação de registro para: {}", request.getEmail());
        authService.register(request);
        logger.info("Usuário registrado com sucesso: {}", request.getEmail());
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackLogin")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        logger.info("Tentativa de login recebida para: {}", request.getEmail());
        LoginResponseDTO response = authService.login(request);
        logger.info("Login bem-sucedido para: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> fallbackAuth(RegisterRequestDTO request, Throwable t) {
        logger.error("Erro no registro para {}: {}", request.getEmail(), t.getMessage(), t);
        return ResponseEntity.status(503).body("Registro temporariamente indisponível");
    }

    public ResponseEntity<LoginResponseDTO> fallbackLogin(LoginRequestDTO request, Throwable t) {
        logger.error("Erro no login para {}: {}", request.getEmail(), t.getMessage(), t);
        return ResponseEntity.status(503).body(null);
    }
}