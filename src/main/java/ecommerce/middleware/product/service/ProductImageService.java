package ecommerce.middleware.product.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ecommerce.middleware.jwt.JwtTokenProvider;
import ecommerce.middleware.product.dto.ProductImageDTO;

@Service
public class ProductImageService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${services.product.url}")
    private String productServiceUrl;

    public ProductImageService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<ProductImageDTO> findAll() {
        String endpoint = productServiceUrl + "/product-images";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<ProductImageDTO[]> response = restTemplate.exchange(
            endpoint,
            HttpMethod.GET,
            entity,
            ProductImageDTO[].class
        );

        return Arrays.asList(response.getBody());
    }

    public Optional<ProductImageDTO> findById(Long id) {
        String endpoint = productServiceUrl + "/product-images/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<ProductImageDTO> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity,
                ProductImageDTO.class
            );
            return Optional.of(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<ProductImageDTO> findImageByProductId(Long productId) {
        String endpoint = productServiceUrl + "/product-images/product/" + productId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<ProductImageDTO> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                entity,
                ProductImageDTO.class
            );
            return Optional.of(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<ProductImageDTO> findAllImagesBySupplierId(Long supplierId) {
        String endpoint = productServiceUrl + "/product-images/supplier/" + supplierId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<ProductImageDTO[]> response = restTemplate.exchange(
            endpoint,
            HttpMethod.GET,
            entity,
            ProductImageDTO[].class
        );

        return Arrays.asList(response.getBody());
    }

    public ProductImageDTO create(ProductImageDTO productImageDTO, Long authUserId) {
        String endpoint = productServiceUrl + "/product-images";

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductImageDTO> entity = new HttpEntity<>(productImageDTO, headers);
        ResponseEntity<ProductImageDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.POST,
            entity,
            ProductImageDTO.class
        );

        return response.getBody();
    }

    public ProductImageDTO update(Long id, ProductImageDTO productImageDTO, Long authUserId) {
        String endpoint = productServiceUrl + "/product-images/" + id;

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductImageDTO> entity = new HttpEntity<>(productImageDTO, headers);
        ResponseEntity<ProductImageDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.PUT,
            entity,
            ProductImageDTO.class
        );

        return response.getBody();
    }

    public void deleteById(Long id, Long authUserId) {
        String endpoint = productServiceUrl + "/product-images/" + id;

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
    
    public byte[] getImageDataAsPngByProductId(Long productId) {
        ProductImageDTO imageDTO = findImageByProductId(productId)
            .orElseThrow(() -> new RuntimeException("Image not found for productId: " + productId));
        return imageDTO.getImageData();
    }
}
