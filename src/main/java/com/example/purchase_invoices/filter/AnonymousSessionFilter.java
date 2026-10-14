package com.example.purchase_invoices.filter;

import com.example.purchase_invoices.response.JsonResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class AnonymousSessionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public AnonymousSessionFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath(); // Caminho da solicitação

        // Se for diferente dos caminhos abaixo pode seguir sem autenticação
        return !(path.equals("/product")
                || path.startsWith("/product/")
                || path.equals("/invoices")
                || path.startsWith("/invoices/"));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Obtem sessão - 'false' = obtenha a sessão existente, mas não crie uma nova
        HttpSession session = request.getSession(false);

        // Nenhuma sessão localizada.
        if (session == null) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JsonResponse<?> error = JsonResponse.error("Sessão inexistente ou expirada.");

            response.getWriter().write(
                    objectMapper.writeValueAsString(error)
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}
