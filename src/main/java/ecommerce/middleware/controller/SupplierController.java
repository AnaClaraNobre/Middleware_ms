package ecommerce.middleware.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ecommerce.middleware.product.service.SupplierService;
import ecommerce.middleware.product.dto.SupplierDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackSupplierList")
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> findAll(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(supplierService.findAll(userId));
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackSupplier")
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> findById(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return supplierService.findById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackSupplier")
    @PostMapping
    public ResponseEntity<SupplierDTO> create(@RequestBody SupplierDTO supplierDTO, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            return ResponseEntity.ok(supplierService.create(supplierDTO, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackSupplier")
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> update(@PathVariable Long id, 
                                            @RequestBody SupplierDTO supplierDTO,
                                            Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            return ResponseEntity.ok(supplierService.update(id, supplierDTO, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackVoid")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            supplierService.deleteById(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Fallback methods
    public ResponseEntity<List<SupplierDTO>> fallbackSupplierList(Throwable t) {
        return ResponseEntity.status(503).body(List.of());
    }

    public ResponseEntity<SupplierDTO> fallbackSupplier(Throwable t) {
        return ResponseEntity.status(503).body(null);
    }

    public ResponseEntity<Void> fallbackVoid(Throwable t) {
        return ResponseEntity.status(503).build();
    }
}
