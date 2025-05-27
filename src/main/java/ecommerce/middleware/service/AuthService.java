package ecommerce.middleware.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ecommerce.middleware.auth.dto.LoginRequestDTO;
import ecommerce.middleware.auth.dto.LoginResponseDTO;
import ecommerce.middleware.auth.dto.RegisterRequestDTO;
import ecommerce.middleware.jwt.JwtTokenProvider;

@Service
public class AuthService {

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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegisterRequestDTO> entity = new HttpEntity<>(request, headers);
        restTemplate.exchange(registerUrl, HttpMethod.POST, entity, String.class);

        LoginRequestDTO loginRequest = new LoginRequestDTO(request.getEmail(), request.getPassword());
        LoginResponseDTO loginResponse = login(loginRequest);

        Long userId = jwtTokenProvider.getUserIdFromToken(loginResponse.getToken());

        userService.createProfile(userId, request.getUsername(), request.getEmail());
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        String endpoint = authServiceUrl + "/auth/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequestDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<LoginResponseDTO> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, LoginResponseDTO.class);

        return response.getBody();
    }
}
