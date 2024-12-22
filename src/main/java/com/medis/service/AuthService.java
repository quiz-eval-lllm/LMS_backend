package com.medis.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> authenticate(String nama, String password) throws Exception {
        String externalAuthUrl = "http://34.121.202.21:8080/realms/integrated-lms-quiz/protocol/openid-connect/token";

        // Prepare request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "password");
        requestBody.add("client_id", "lms-client");
        requestBody.add("client_secret", "v5hFzsWNorubVLNMjrRWtk5WXYqdkFDS");
        requestBody.add("username", nama);
        requestBody.add("password", password);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Make API call
            ResponseEntity<Map> response = restTemplate.exchange(
                    externalAuthUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new Exception("External authentication failed with status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new Exception("Invalid credentials provided to external service.", e);
        } catch (Exception e) {
            throw new Exception("An error occurred during external authentication.", e);
        }
    }
}
