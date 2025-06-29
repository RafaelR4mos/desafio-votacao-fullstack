package com.dbserver.votacao.exception.handler;

import com.dbserver.votacao.exception.BusinessRuleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionResponseHandler {

    // In case, client not send a required body, with @RequestBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseHandler> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException occurred: {}", ex.getMessage());
        ErrorResponseHandler errorResponse = ErrorResponseHandler.builder().statusCode(HttpStatus.BAD_REQUEST.value()).
                error("MESSAGE_NOT_READABLE").
                message("Corpo da solicitação com JSON inválido.").
                timestamp(System.currentTimeMillis()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // In case, a validation fails in fields
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseHandler> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());
        Map<String, List<String>> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            fieldErrors.computeIfAbsent(((FieldError) error).getField(),
                    k -> new ArrayList<>()).add(error.getDefaultMessage());

        });

        ErrorResponseHandler errorResponse = ErrorResponseHandler.builder().statusCode(HttpStatus.BAD_REQUEST.value()).
                error("FIELD_VALIDATION_ERROR").
                message("Erro na validação dos dados enviados. Verifique os campos informados.").
                timestamp(System.currentTimeMillis()).
                fieldErrors(fieldErrors).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Generic Exception - Used mainly for application business rules
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponseHandler> handleRegraDeNegocioException(BusinessRuleException ex) {
        log.error("RegraDeNegocioException occurred: {}", ex.getMessage());
        ErrorResponseHandler errorResponse = ErrorResponseHandler.builder().statusCode(HttpStatus.BAD_REQUEST.value())
                .error("RegraDeNegocioException").message(ex.getMessage()).timestamp(System.currentTimeMillis()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    // DATABASE EXCEPTIONS
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseHandler> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException occurred: {}", ex.getMessage());
        String message = "Violação de integridade de dados";

        if (ex.getMessage().contains("associate_cpf_key")) {
            message = "Não é possível utilizar este CPF para completar o cadastro";
        } else if (ex.getMessage().contains("associate_email_key")) {
            message = "Não é possível utilizar este e-mail para completar o cadastro";
        } else if (ex.getMessage().contains("agenda_slug_key")) {
            message = "Já existe uma pauta cadastrada com exatamente este título";
        }

        ErrorResponseHandler errorResponse = ErrorResponseHandler.builder().statusCode(HttpStatus.CONFLICT.value())
                .error("DataIntegrityViolationException").message(message).timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
