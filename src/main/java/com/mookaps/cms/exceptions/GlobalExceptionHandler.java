package com.mookaps.cms.exceptions;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.mookaps.cms.helpers.Log;
import com.mookaps.cms.http.JsonResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegiry(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        String message = "";
        if (rootCause != null) {
            message = rootCause.getMessage();
        }
        JsonResponse<?> json = new JsonResponse<>(400, message, 0, Collections.emptyList());
        return ResponseEntity.ok().body(json);
    }

    @ExceptionHandler(QueryException.class)
    public ResponseEntity<?> handleQueryException(QueryException ex) {
        JsonResponse<?> json = new JsonResponse<>(400, ex.getMessage(), 0, Collections.emptyList());
        return ResponseEntity.ok().body(json);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        JsonResponse<?> json = new JsonResponse<>(400, "Validation error: " + message, 0, Collections.emptyList());
        return ResponseEntity.ok().body(json);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuth(AuthenticationException ex) {
        JsonResponse<?> json = new JsonResponse<>(401, "Unauthorized: " + ex.getMessage(), 0, Collections.emptyList());
        return ResponseEntity.ok().body(json);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        JsonResponse<?> json = new JsonResponse<>(403, "Access Denied", 0, Collections.emptyList());
        return ResponseEntity.ok().body(json);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFound(NoHandlerFoundException ex) {
        JsonResponse<?> json = new JsonResponse<>(404, "Path Not Found", 0, Collections.emptyList());
        return ResponseEntity.ok().body(json);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        JsonResponse<?> json = new JsonResponse<>(404, ex.getMessage(), 0, Collections.emptyList());
        return ResponseEntity.ok().body(json);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        JsonResponse<?> json = new JsonResponse<>(405, "Method Not Allowed: " + ex.getMethod(), 0,
                Collections.emptyList());
        return ResponseEntity.ok().body(json);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        String message = "Internal Server Error:: " + ex.getMessage();
        if (ex instanceof UnexpectedRollbackException) {
            message = "Delete Failed";
        } else if (ex instanceof NumberFormatException) {
            message = "Failed to Fetch Number";
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            String[] aa = ex.getMessage().split(";");
            String error1 = aa[0];
            String error2 = ex.getCause().getMessage();
            message = error1 + " " + error2;
        }
        Log.info(ex.getClass().getName());

        List<?> data = Collections.emptyList();
        JsonResponse<?> json = new JsonResponse<>(500, message, 0, data);
        return ResponseEntity.ok().body(json);
    }

    @ExceptionHandler(SQLGrammarException.class)
    public ResponseEntity<?> handleSQLGrammarException(SQLGrammarException ex) {
        JsonResponse<?> json = new JsonResponse<>(500, "Error SQL" + ex.getMessage(), 0, Collections.emptyList());
        return ResponseEntity.ok().body(json);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccess(DataAccessException ex) {
        Throwable root = ex.getRootCause();
        if (root instanceof SQLException) {
            JsonResponse<?> json = new JsonResponse<>(501, "Error SQL" + root.getMessage(), 0, Collections.emptyList());
            return ResponseEntity.ok().body(json);
        }
        JsonResponse<?> json = new JsonResponse<>(501, "Error SQL" + ex.getMessage(), 0, Collections.emptyList());
        return ResponseEntity.ok().body(json);
    }
}
