package ecommerce.middleware.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ecommerce.middleware.service.UserService;
import ecommerce.middleware.user.dto.AddressDTO;
import ecommerce.middleware.user.dto.ProfileDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackUser")
    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackUser")
    @PutMapping("/profile")
    public ResponseEntity<ProfileDTO> updateProfile(@RequestBody ProfileDTO profileDTO,
                                                    Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(userService.updateProfile(userId, profileDTO));
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackUserList")
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getAddresses(userId));
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackUserAddress")
    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO,
                                                    Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        addressDTO.setAuthUserId(userId);
        return ResponseEntity.ok(userService.createAddress(userId, addressDTO));
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackUserAddress")
    @PutMapping("/addresses/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id,
                                                    @RequestBody AddressDTO dto,
                                                    Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        dto.setAuthUserId(userId);
        return ResponseEntity.ok(userService.updateAddress(userId, id, dto));
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackVoid")
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.deleteAddress(userId, id);
        return ResponseEntity.noContent().build();
    }

    @CircuitBreaker(name = "default", fallbackMethod = "fallbackVoid")
    @PutMapping("/profile/default-address/{id}")
    public ResponseEntity<Void> setDefaultAddress(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.setDefaultAddress(userId, id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<ProfileDTO> fallbackUser(Throwable t) {
    return ResponseEntity.status(503).body(null);
    }

    public ResponseEntity<List<AddressDTO>> fallbackUserList(Throwable t) {
        return ResponseEntity.status(503).body(List.of());
    }

    public ResponseEntity<AddressDTO> fallbackUserAddress(Throwable t) {
        return ResponseEntity.status(503).body(null);
    }

    public ResponseEntity<Void> fallbackVoid(Throwable t) {
        return ResponseEntity.status(503).build();
    }
}
