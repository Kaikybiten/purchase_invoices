package com.example.purchase_invoices.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"success", "message", "timestamp", "data"})
public class JsonResponse<T> {

    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    private List<T> data;

    private JsonResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = new ArrayList<>();
        this.data.add(data);
    }

    private JsonResponse(boolean success, String message, List<T> data) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    public static <T> JsonResponse<T> success(T data) {
        return new JsonResponse<>(true, "Operação realizada com sucesso", data);
    }

    public static <T> JsonResponse<T> success(List<T> data) {
        return new JsonResponse<>(true, "Operação realizada com sucesso", data);
    }

    public static <T> JsonResponse<T> success(String message, List<T> data) {
        return new JsonResponse<>(true, message, data);
    }

    public static <T> JsonResponse<T> error(String message) {
        return new JsonResponse<>(false, message, null);
    }

    public static <T> JsonResponse<T> error(String message, List<T> data) {
        return new JsonResponse<>(false, message, data);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<T> getData() { return data; }
}
