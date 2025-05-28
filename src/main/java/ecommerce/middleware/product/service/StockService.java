package ecommerce.middleware.product.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ecommerce.middleware.jwt.JwtTokenProvider;
import ecommerce.middleware.product.dto.StockDTO;

@Service
public class StockService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${services.product.url}")
    private String productServiceUrl;

    public StockService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<StockDTO> findAll(Long authUserId) {
        String endpoint = productServiceUrl + "/stock";
        
        String token = jwtTokenProvider.generateToken(authUserId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<StockDTO[]> response = restTemplate.exchange(
            endpoint,
            HttpMethod.GET,
            entity,
            StockDTO[].class
        );

        return Arrays.asList(response.getBody());
    }

    public Optional<StockDTO> findById(Long id, Long authUserId) {
        String endpoint = productServiceUrl + "/stock/" + id;

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<StockDTO> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity,
                StockDTO.class
            );
            return Optional.of(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public StockDTO create(StockDTO stockDTO, Long authUserId) {
        String endpoint = productServiceUrl + "/stock";

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StockDTO> entity = new HttpEntity<>(stockDTO, headers);
        ResponseEntity<StockDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.POST,
            entity,
            StockDTO.class
        );

        return response.getBody();
    }

    public StockDTO update(Long id, StockDTO stockDTO, Long authUserId) {
        String endpoint = productServiceUrl + "/stock/" + id;

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StockDTO> entity = new HttpEntity<>(stockDTO, headers);
        ResponseEntity<StockDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.PUT,
            entity,
            StockDTO.class
        );

        return response.getBody();
    }

    public void deleteById(Long id, Long authUserId) {
        String endpoint = productServiceUrl + "/stock/" + id;

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        restTemplate.exchange(
            endpoint,
            HttpMethod.DELETE,
            entity,
            Void.class
        );
    }
}
