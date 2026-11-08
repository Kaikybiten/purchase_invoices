package com.example.purchase_invoices.exception;


import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.example.purchase_invoices.response.DataError;
import com.example.purchase_invoices.response.JsonResponse;




// Captura as exceções de toda aplicação
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Criando log para controle
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Metodo para capturar exceções de argumentos invalidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResponse<List<DataError>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {

        log.warn("Erro na validação de dados: {}", exception.getMessage());

        List<DataError> dataError = new ArrayList<>();

        List<FieldError> allErrors = exception.getBindingResult().getFieldErrors();

        allErrors.forEach(error -> {

            String field = error.getField();
            String defaultMessage = error.getDefaultMessage();

            dataError.add(new DataError(field, defaultMessage));
        });

        JsonResponse<List<DataError>> response = JsonResponse.error(
                "Erro na validação de dados",
                dataError
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<JsonResponse<Void>> handleMalformedURLException(
            MalformedURLException exception
    ) {

        log.warn("URL inválida: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                JsonResponse.error("A URL informada é inválida.")
        );
    }

    // Metodo para capturar exceções de requisão não localizada
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<JsonResponse<Void>> handleMethodNotFoundException(
            EntityNotFoundException exception
    ) {

        log.warn("Recurso não encontrado: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                JsonResponse.error("O recurso solicitado não foi encontrado.")        );
    }

    @ExceptionHandler(InvoiceReadException.class)
    public ResponseEntity<JsonResponse<Void>> handleInvoiceReadException(
            InvoiceReadException exception
    ) {

        log.error("Erro ao realizar leitura da nota fiscal", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                JsonResponse.error(exception.getMessage())
        );
    }

}
