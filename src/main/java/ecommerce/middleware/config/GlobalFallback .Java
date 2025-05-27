package ecommerce.middleware.fallback;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GlobalFallback {

    public ResponseEntity<Object> fallback(Throwable t) {
        return ResponseEntity.status(503).body("Serviço temporariamente indisponível");
    }
}
