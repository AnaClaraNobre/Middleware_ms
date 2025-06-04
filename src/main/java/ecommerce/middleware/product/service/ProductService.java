package ecommerce.middleware.product.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ecommerce.middleware.jwt.JwtTokenProvider;
import ecommerce.middleware.product.dto.ProductDTO;

@Service
public class ProductService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${services.product.url}")
    private String productServiceUrl;

    public ProductService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<ProductDTO> findAll() {
        String endpoint = productServiceUrl + "/product";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<ProductDTO[]> response = restTemplate.exchange(
            endpoint,
            HttpMethod.GET,
            entity,
            ProductDTO[].class
        );

        return Arrays.asList(response.getBody());
    }

    public Optional<ProductDTO> findById(Long id) {
        String endpoint = productServiceUrl + "/product/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<ProductDTO> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity,
                ProductDTO.class
            );
            return Optional.of(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public ProductDTO create(ProductDTO productDTO, Long authUserId) {
        String endpoint = productServiceUrl + "/product";

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductDTO> entity = new HttpEntity<>(productDTO, headers);
        ResponseEntity<ProductDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.POST,
            entity,
            ProductDTO.class
        );

        return response.getBody();
    }

    public ProductDTO update(Long id, ProductDTO productDTO, Long authUserId) {
        String endpoint = productServiceUrl + "/product/" + id;    

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductDTO> entity = new HttpEntity<>(productDTO, headers);
        ResponseEntity<ProductDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.PUT,
            entity,
            ProductDTO.class
        );

        return response.getBody();
    }

    public void deleteById(Long id, Long authUserId) {
        String endpoint = productServiceUrl + "/product/" + id;

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
