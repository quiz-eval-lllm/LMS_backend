package com.medis.util;

import java.util.Base64;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RoleExtractor {

    // Decode JWT and extract `client_dokter` role
    public static String extractClientDokterRole(String token) throws Exception {
        // Split the JWT into its parts
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT token format.");
        }

        // Decode the payload part
        String payload = new String(Base64.getDecoder().decode(parts[1]));

        // Parse the JSON payload
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jwtPayload = objectMapper.readValue(payload, Map.class);

        // Navigate to `resource_access.lms-client.roles`
        Map<String, Object> resourceAccess = (Map<String, Object>) jwtPayload.get("resource_access");
        if (resourceAccess == null || !resourceAccess.containsKey("lms-client")) {
            throw new IllegalArgumentException("Resource access for 'lms-client' not found.");
        }

        Map<String, Object> lmsClient = (Map<String, Object>) resourceAccess.get("lms-client");
        Collection<String> roles = (Collection<String>) lmsClient.get("roles");

        // Check if `client_dokter` is present in roles
        if (roles.contains("client_dokter")) {
            return "DOKTER";
        }

        // Check if `client_dokter` is present in roles
        if (roles.contains("client_admin")) {
            return "ADMIN";
        }

        return null; // Role not found
    }

    // Decode JWT and extract `email`
    public static String extractEmail(String token) throws Exception {
        // Split the JWT into its parts
        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            return null; // Invalid JWT format
        }

        // Decode the payload part
        String payload = new String(Base64.getDecoder().decode(parts[1]));

        // Parse the JSON payload
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jwtPayload = objectMapper.readValue(payload, Map.class);

        // Extract the email
        return (String) jwtPayload.get("email");
    }

}
