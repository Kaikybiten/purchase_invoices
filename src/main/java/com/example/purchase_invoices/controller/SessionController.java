package com.example.purchase_invoices.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping
    public ResponseEntity<?> createSession(HttpServletRequest request) {

        // Cria sessão
        HttpSession session = request.getSession(true);

        return ResponseEntity.ok(
                Map.of("message", "Sessão criada com sucesso.")
        );
    }
}
