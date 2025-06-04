package ecommerce.middleware.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ecommerce.middleware.product.service.ProductImageService;
import ecommerce.middleware.product.dto.ProductImageDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
// import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/product-image")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductImageList")
    @GetMapping
    public ResponseEntity<List<ProductImageDTO>> findAll() {
        return ResponseEntity.ok(productImageService.findAll());
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductImage")
    @GetMapping("/{id}")
    public ResponseEntity<ProductImageDTO> findById(@PathVariable Long id) {
        return productImageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductImage")
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductImageDTO> findByProductId(@PathVariable Long productId) {
        return productImageService.findImageByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackProductImageList")
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<ProductImageDTO>> findAllBySupplierId(@PathVariable Long supplierId) {
        return ResponseEntity.ok(productImageService.findAllImagesBySupplierId(supplierId));
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

    @GetMapping("/{id}/image")
    // @Operation(summary = "Get image as PNG", description = "Retrieves the image in PNG format by its ID")
    public ResponseEntity<byte[]> getImageAsPng(@PathVariable Long id) {
        try {
            byte[] imageData = productImageService.getImageDataAsPng(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(imageData.length);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
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
