package ru.zyryanova.TransferService.errors;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "CONFLICT",
                "message", "Запись уже существует или нарушено ограничение целостности"
        ));
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleJson(JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "SERIALIZATION_ERROR",
                "message", "Не удалось сериализовать событие"
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e){
        var errors = e.getBindingResult().getFieldErrors().stream()
                .collect(java.util.stream.Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage(),
                        (a,b) -> a
                ));

        return ResponseEntity.badRequest().body(Map.of(
                "error","VALIDATION_ERROR",
                "fields", errors
        ));
    }



}