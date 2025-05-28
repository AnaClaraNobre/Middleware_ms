package ecommerce.middleware.product.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ecommerce.middleware.jwt.JwtTokenProvider;
import ecommerce.middleware.product.dto.SupplierDTO;

@Service
public class SupplierService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${services.product.url}")
    private String productServiceUrl;

    public SupplierService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<SupplierDTO> findAll(Long authUserId) {
        String endpoint = productServiceUrl + "/supplier";
        
        String token = jwtTokenProvider.generateToken(authUserId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<SupplierDTO[]> response = restTemplate.exchange(
            endpoint,
            HttpMethod.GET,
            entity,
            SupplierDTO[].class
        );

        return Arrays.asList(response.getBody());
    }

    public Optional<SupplierDTO> findById(Long id, Long authUserId) {
        String endpoint = productServiceUrl + "/supplier/" + id;

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<SupplierDTO> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity,
                SupplierDTO.class
            );
            return Optional.of(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public SupplierDTO create(SupplierDTO supplierDTO, Long authUserId) {
        String endpoint = productServiceUrl + "/supplier";

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SupplierDTO> entity = new HttpEntity<>(supplierDTO, headers);
        ResponseEntity<SupplierDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.POST,
            entity,
            SupplierDTO.class
        );

        return response.getBody();
    }

    public SupplierDTO update(Long id, SupplierDTO supplierDTO, Long authUserId) {
        String endpoint = productServiceUrl + "/supplier/" + id;

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SupplierDTO> entity = new HttpEntity<>(supplierDTO, headers);
        ResponseEntity<SupplierDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.PUT,
            entity,
            SupplierDTO.class
        );

        return response.getBody();
    }

    public void deleteById(Long id, Long authUserId) {
        String endpoint = productServiceUrl + "/supplier/" + id;

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
