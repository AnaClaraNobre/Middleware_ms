package ecommerce.middleware.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ecommerce.middleware.product.service.ProductImageService;
import ecommerce.middleware.product.dto.ProductImageDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/product-image")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductImageList")
    @GetMapping
    public ResponseEntity<List<ProductImageDTO>> findAll(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(productImageService.findAll(userId));
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductImage")
    @GetMapping("/{id}")
    public ResponseEntity<ProductImageDTO> findById(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return productImageService.findById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductImage")
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductImageDTO> findByProductId(@PathVariable Long productId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return productImageService.findImageByProductId(productId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductImageList")
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<ProductImageDTO>> findAllBySupplierId(@PathVariable Long supplierId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(productImageService.findAllImagesBySupplierId(supplierId, userId));
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductImage")
    @PostMapping
    public ResponseEntity<ProductImageDTO> create(@RequestBody ProductImageDTO productImageDTO, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            return ResponseEntity.ok(productImageService.create(productImageDTO, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductImage")
    @PutMapping("/{id}")
    public ResponseEntity<ProductImageDTO> update(@PathVariable Long id, 
                                                @RequestBody ProductImageDTO productImageDTO,
                                                Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            return ResponseEntity.ok(productImageService.update(id, productImageDTO, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackVoid")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            productImageService.deleteById(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Fallback methods
    public ResponseEntity<List<ProductImageDTO>> fallbackProductImageList(Throwable t) {
        return ResponseEntity.status(503).body(List.of());
    }

    public ResponseEntity<ProductImageDTO> fallbackProductImage(Throwable t) {
        return ResponseEntity.status(503).body(null);
    }

    public ResponseEntity<Void> fallbackVoid(Throwable t) {
        return ResponseEntity.status(503).build();
    }
}
