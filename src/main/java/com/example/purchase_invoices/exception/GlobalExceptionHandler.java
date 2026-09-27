package com.example.purchase_invoices.exception;

import jakarta.el.MethodNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import com.example.purchase_invoices.response.DataError;
import com.example.purchase_invoices.response.JsonResponse;
import org.springframework.web.context.request.WebRequest;

import javax.swing.text.html.parser.Entity;


// Captura as exceções de toda aplicação
@RestControllerAdvice
public class GlobalExceptionHandler {


    // Metodo para capturar exceções de requisão não localizada
    @ExceptionHandler(MethodNotFoundException.class)
    public ResponseEntity<JsonResponse<Void>> handleMethodNotFoundException(
            MethodNotFoundException exception
    ) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                JsonResponse.error(exception.getMessage())
        );
    }

    // Metodo para capturar exceções de argumentos invalidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResponse<DataError>> handleMethodArgumentNotValidException (
            MethodArgumentNotValidException exception
    ) {

        List<DataError> dataError = new ArrayList<>();

        // Objetos com os erros
        List<FieldError> allErrors = exception.getBindingResult().getFieldErrors();
        // Iterando pois podem vir multiplos erros de validação
        allErrors.forEach(error -> {

            String field = error.getField();
            String defaultMessage = error.getDefaultMessage();

            dataError.add(new DataError(field, defaultMessage));
        });

        JsonResponse<DataError> response = JsonResponse.error(
                "Erro da validação de dados", dataError
            );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
