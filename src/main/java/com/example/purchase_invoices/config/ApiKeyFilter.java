package com.example.purchase_invoices.config;

import com.example.purchase_invoices.response.JsonResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${API_KEY}")
    private String expectedApiKey;

    private final ObjectMapper objectMapper;

    public ApiKeyFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String apiKey = request.getHeader("X-API-Key");

        System.out.println("equals: " + expectedApiKey.equals(apiKey));
        System.out.println("header: [" + apiKey.substring(0, 4) + "..." + apiKey.substring(60) + "]");
        System.out.println("expected: [" + expectedApiKey.substring(0, 4) + "..." + expectedApiKey.substring(60) + "]");

        if (!expectedApiKey.equals(apiKey)) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            JsonResponse<?> error = JsonResponse.error("Invalid API Key");

            response.getWriter().write(objectMapper.writeValueAsString(error));

            return;
        }

        filterChain.doFilter(request, response);
    }
}