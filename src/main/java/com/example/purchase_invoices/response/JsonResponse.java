package com.example.purchase_invoices.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;
import java.time.ZoneId;

@JsonPropertyOrder({"success", "message", "timestamp", "data"})
public class JsonResponse<T> {

    private final boolean success;
    private final String message;
    private final LocalDateTime timestamp;
    private final T data;

    private JsonResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        this.data = data;
    }

    public static <T> JsonResponse<T> success(T data) {
        return new JsonResponse<>(true, "Operação realizada com sucesso", data);
    }

    public static <T> JsonResponse<T> success(String message, T data) {
        return new JsonResponse<>(true, message, data);
    }

    public static <T> JsonResponse<T> error(String message) {
        return new JsonResponse<>(false, message, null);
    }

    public static <T> JsonResponse<T> error(String message, T data) {
        return new JsonResponse<>(false, message, data);
    }


    public boolean isSuccess() {return success;}
    public String getMessage() {return message;}
    public LocalDateTime getTimestamp() {return timestamp;}
    public T getData() {return data;}
}