package ecommerce.middleware.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import ecommerce.middleware.auth.dto.LoginRequestDTO;
import ecommerce.middleware.auth.dto.LoginResponseDTO;
import ecommerce.middleware.auth.dto.RegisterRequestDTO;
import ecommerce.middleware.jwt.JwtTokenProvider;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${services.auth.url}")
    private String authServiceUrl;

    public AuthService(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void register(RegisterRequestDTO request) {
        String registerUrl = authServiceUrl + "/auth/register";
        logger.info("Iniciando registro de usuário: {}", request.getEmail());
        logger.debug("Enviando POST para {}", registerUrl);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RegisterRequestDTO> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> registerResponse = restTemplate.exchange(registerUrl, HttpMethod.POST, entity, String.class);
            logger.info("Registro concluído. Status: {}", registerResponse.getStatusCodeValue());
        } catch (RestClientException ex) {
            logger.error("Erro durante o registro no serviço de autenticação: {}", ex.getMessage(), ex);
            throw ex;
        }

        LoginRequestDTO loginRequest = new LoginRequestDTO(request.getEmail(), request.getPassword());
        LoginResponseDTO loginResponse = login(loginRequest);

        Long userId = jwtTokenProvider.getUserIdFromToken(loginResponse.getToken());

        logger.info("Criando perfil do usuário com ID: {}", userId);
        userService.createProfile(userId, request.getUsername(), request.getEmail());
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        String endpoint = authServiceUrl + "/auth/login";
        logger.info("Iniciando login do usuário: {}", request.getEmail());
        logger.debug("Enviando POST para {}", endpoint);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequestDTO> entity = new HttpEntity<>(request, headers);

            ResponseEntity<LoginResponseDTO> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, LoginResponseDTO.class);
            logger.info("Login realizado com sucesso. Status: {}", response.getStatusCodeValue());
            return response.getBody();
        } catch (RestClientException ex) {
            logger.error("Erro durante o login no serviço de autenticação: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
