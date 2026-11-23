package com.example.purchase_invoices.filter;

import com.example.purchase_invoices.response.JsonResponse;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public RateLimitFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // ‘String’ - IP do cliente
    // Bucket - Reservatório de ‘tokens’ para controlar as requisições
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();


    private Bucket createBucket() {

        // Construindo consumo de ‘tokens’
        Bandwidth limit = Bandwidth.builder()
                .capacity(3) // Maximo 3 ‘tokens’
                .refillIntervally(3, Duration.ofMinutes(1)) // Adiciona 3 ‘tokens’ a cada 1 minuto
                .build();

        // Retornando Bucket configurado
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        boolean protectedPath = path.equals("/product")
                                    || path.startsWith("/product/")
                                        || path.equals("/invoices");

        return !protectedPath;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Obtendo IP
        String clientIp = request.getRemoteAddr();

        // Obtendo o Bucket referente ao IP ou criando outro se necessário
        Bucket bucket = buckets.computeIfAbsent(
                clientIp,
                key -> createBucket()
        );

        // Se houver ‘token’ disponível, consome ele
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(429);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonResponse<?> error = JsonResponse.error("Too many requests");

        response.getWriter().write(
                objectMapper.writeValueAsString(error)
        );
    }
}


