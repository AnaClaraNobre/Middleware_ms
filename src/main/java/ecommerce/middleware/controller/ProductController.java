package ecommerce.middleware.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import ecommerce.middleware.product.dto.ProductDTO;
import ecommerce.middleware.product.service.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductList")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProduct")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProduct")
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            return ResponseEntity.ok(productService.create(productDTO, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProduct")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, 
                                           @RequestBody ProductDTO productDTO,
                                           Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            return ResponseEntity.ok(productService.update(id, productDTO, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackVoid")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            productService.deleteById(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Fallback methods
    public ResponseEntity<List<ProductDTO>> fallbackProductList(Throwable t) {
        return ResponseEntity.status(503).body(List.of());
    }

    public ResponseEntity<ProductDTO> fallbackProduct(Throwable t) {
        return ResponseEntity.status(503).body(null);
    }

    public ResponseEntity<Void> fallbackVoid(Throwable t) {
        return ResponseEntity.status(503).build();
    }
}
