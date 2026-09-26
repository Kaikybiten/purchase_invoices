package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import com.example.purchase_invoices.response.DataError;
import response.JsonResponse;


// Captura as exceções de toda aplicação
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Metodo para argumentos invalidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResponse<DataError>> handleMethodArgumentNotValidException (
            MethodArgumentNotValidException exception
    ) {

        // Objetos com os erros
        List<FieldError> allErrors = exception.getBindingResult().getFieldErrors();

        List<DataError> dataError = new ArrayList<>();
        allErrors.forEach(error -> {

            String field = error.getField();
            String defaultMessage = error.getDefaultMessage();

            dataError.add(new DataError(field, defaultMessage));
        });

        JsonResponse<DataError> response = JsonResponse.error("Erro da validação de dados", dataError);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
