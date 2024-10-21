package com.nextstep.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Base64;
import java.util.List;
import java.util.Map;

public class SecurityUtils {
    public static boolean hasRole(String role, String token) {
        // Decodificar el token y verificar los roles
        try {
            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});

            List<String> roles = (List<String>) payloadMap.get("roles");
            return roles != null && roles.contains(role);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
