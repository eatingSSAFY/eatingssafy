package com.A204.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionManager {
    private final Logger logger = LoggerFactory.getLogger(ExceptionManager.class);

    /**
     * Validation 에러 발생 시 동작
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();
        if (bindingResult.hasErrors()) logger.error("Validation error");
        bindingResult.getAllErrors().forEach(o -> {
            FieldError error = (FieldError) o;
            errorMap.put(error.getField(), error.getDefaultMessage());
            logger.error(error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errorMap);
    }

    /**
     * CustomException
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorCode", ex.getStatus().toString());
        if (ex.getMessage() != null) {
            errorMap.put("message", ex.getMessage());
            logger.error(ex.getMessage());
        }
        return ResponseEntity.status(ex.getStatus()).body(errorMap);
    }

    /**
     * Unique Constraint(:DataIntegrityViolationException) Handler
     */
    @ExceptionHandler
    public ResponseEntity<?> uniqueConstraintExceptionHandler(DataIntegrityViolationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorCode", HttpStatus.CONFLICT.toString());
        errorMap.put("message", "유니크 키 제약조건 위배로 DB 등록에 실패했습니다.");
        logger.error("유니크 키 제약조건 위배로 DB 등록에 실패했습니다.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMap);
    }
}