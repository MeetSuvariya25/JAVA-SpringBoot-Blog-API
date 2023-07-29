package com.example.BlogAPI.exceptions;

import com.example.BlogAPI.dto.APIResponse;
import com.example.BlogAPI.helper.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    ZoneId zoneId = ZoneId.systemDefault();
    long epoch;

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFound(UserNotFoundException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.NOT_FOUND.value(), epoch, Constants.USER_NOT_FOUND, request.getDescription(false)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.BAD_REQUEST.value(), epoch, "Email alredy registered.", request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(com.example.BlogAPI.exceptions.UserNotSavedException.class)
    public ResponseEntity<?> userNotSavedException(UserNotSavedException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.BAD_GATEWAY.value(), epoch, "User not saved.", request.getDescription(false)), HttpStatus.BAD_GATEWAY);
    }


    @ExceptionHandler(com.example.BlogAPI.exceptions.SamePasswordException.class)
    public ResponseEntity<?> samePasswordException(SamePasswordException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.BAD_GATEWAY.value(), epoch, "Both passwords are same", request.getDescription(false)), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(com.example.BlogAPI.exceptions.PasswordNullException.class)
    public ResponseEntity<?> passwordNullException(PasswordNullException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.BAD_GATEWAY.value(), epoch, "Null password is not acceptable.", request.getDescription(false)), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(com.example.BlogAPI.exceptions.CategoryNotSavedException.class)
    public ResponseEntity<?> categoryNotSavedException(CategoryNotSavedException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.BAD_GATEWAY.value(), epoch, "Category not saved.", request.getDescription(false)), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(com.example.BlogAPI.exceptions.CategoryNotFoundException.class)
    public ResponseEntity<?> categoryNotFoundException(CategoryNotFoundException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.BAD_GATEWAY.value(), epoch, "Category not found.", request.getDescription(false)), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(com.example.BlogAPI.exceptions.PostNotSavedException.class)
    public ResponseEntity<?> postNotSavedException(PostNotSavedException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.BAD_GATEWAY.value(), epoch, "Post not saved.", request.getDescription(false)), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(com.example.BlogAPI.exceptions.PostNotFoundException.class)
    public ResponseEntity<?> postNotFoundException(PostNotFoundException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.BAD_GATEWAY.value(), epoch, "Post not found.", request.getDescription(false)), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(com.example.BlogAPI.exceptions.PostNotDeletedException.class)
    public ResponseEntity<?> postNotDeletedException(PostNotDeletedException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.BAD_GATEWAY.value(), epoch, "Post not deleted.", request.getDescription(false)), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(com.example.BlogAPI.exceptions.PasswordIncorrectException.class)
    public ResponseEntity<?> passwordIncorrectException(PasswordIncorrectException ex, WebRequest request) {
        epoch = LocalDateTime.now().atZone(zoneId).toEpochSecond();
        return new ResponseEntity<>(new APIResponse(HttpStatus.BAD_GATEWAY.value(), epoch, "Password Incorrect.", request.getDescription(false)), HttpStatus.BAD_GATEWAY);
    }


}
