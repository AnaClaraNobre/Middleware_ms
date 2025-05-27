package ecommerce.middleware.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ecommerce.middleware.jwt.JwtTokenProvider;
import ecommerce.middleware.user.dto.AddressDTO;
import ecommerce.middleware.user.dto.ProfileDTO;


@Service
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${services.user.url}")
    private String userServiceUrl;

    public UserService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ProfileDTO getProfile(Long authUserId) {
        String generatedToken = jwtTokenProvider.generateToken(authUserId);
        String endpoint = userServiceUrl + "/profiles/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generatedToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<ProfileDTO> response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, ProfileDTO.class);

        return response.getBody();
    }
    
    public void createProfile(Long authUserId, String username, String email) {
        String endpoint = userServiceUrl + "/profiles";

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername(username);
        profileDTO.setEmail(email);
        profileDTO.setAuthUserId(authUserId);

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProfileDTO> entity = new HttpEntity<>(profileDTO, headers);
        restTemplate.exchange(endpoint, HttpMethod.POST, entity, ProfileDTO.class);
    }
    
    public ProfileDTO updateProfile(Long authUserId, ProfileDTO profileDTO) {
        String endpoint = userServiceUrl + "/profiles/me";

        profileDTO.setAuthUserId(authUserId);

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProfileDTO> entity = new HttpEntity<>(profileDTO, headers);

        ResponseEntity<ProfileDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.PUT,
            entity,
            ProfileDTO.class
        );

        return response.getBody();
    }
    
    public List<AddressDTO> getAddresses(Long authUserId) {
        String endpoint = userServiceUrl + "/addresses";

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<AddressDTO[]> response = restTemplate.exchange(
            endpoint,
            HttpMethod.GET,
            entity,
            AddressDTO[].class
        );

        return Arrays.asList(response.getBody());
    }
    
    public AddressDTO createAddress(Long authUserId, AddressDTO addressDTO) {
        String endpoint = userServiceUrl + "/addresses";

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AddressDTO> entity = new HttpEntity<>(addressDTO, headers);
        ObjectMapper mapper = new ObjectMapper();
        try {
			System.out.println("JSON ENVIADO: " + mapper.writeValueAsString(addressDTO));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ResponseEntity<AddressDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.POST,
            entity,
            AddressDTO.class
        );

        return response.getBody();
    }
    
    public AddressDTO updateAddress(Long authUserId, Long id, AddressDTO dto) {
        String endpoint = userServiceUrl + "/addresses/" + id;

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AddressDTO> entity = new HttpEntity<>(dto, headers);

        ResponseEntity<AddressDTO> response = restTemplate.exchange(
            endpoint,
            HttpMethod.PUT,
            entity,
            AddressDTO.class
        );

        return response.getBody();
    }
    
    public void deleteAddress(Long authUserId, Long id) {
        String endpoint = userServiceUrl + "/addresses/" + id;

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(endpoint, HttpMethod.DELETE, entity, Void.class);
    }
    
    public void setDefaultAddress(Long authUserId, Long addressId) {
        String endpoint = userServiceUrl + "/profiles/me/default-address/" + addressId;

        String token = jwtTokenProvider.generateToken(authUserId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(endpoint, HttpMethod.PUT, entity, Void.class);
    }


}