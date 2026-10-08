package com.example.purchase_invoices.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"success", "message", "timestamp", "data"})
public class JsonResponse<T> {

    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    private T data;

    private JsonResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
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