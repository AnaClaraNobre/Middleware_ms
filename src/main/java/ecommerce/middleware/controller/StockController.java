package ecommerce.middleware.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ecommerce.middleware.product.service.StockService;
import ecommerce.middleware.product.dto.StockDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackStockList")
    @GetMapping
    public ResponseEntity<List<StockDTO>> findAll(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(stockService.findAll(userId));
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackStock")
    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> findById(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return stockService.findById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackStock")
    @PostMapping
    public ResponseEntity<StockDTO> create(@RequestBody StockDTO stockDTO, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            return ResponseEntity.ok(stockService.create(stockDTO, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackStock")
    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> update(@PathVariable Long id, 
                                         @RequestBody StockDTO stockDTO,
                                         Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            return ResponseEntity.ok(stockService.update(id, stockDTO, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackVoid")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            stockService.deleteById(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Fallback methods
    public ResponseEntity<List<StockDTO>> fallbackStockList(Throwable t) {
        return ResponseEntity.status(503).body(List.of());
    }

    public ResponseEntity<StockDTO> fallbackStock(Throwable t) {
        return ResponseEntity.status(503).body(null);
    }

    public ResponseEntity<Void> fallbackVoid(Throwable t) {
        return ResponseEntity.status(503).build();
    }
}
