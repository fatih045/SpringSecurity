package com.example.security1.exception;


import com.example.security1.Dto.Response.CustomResponse;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    //404 - user not found

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handlerUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
       CustomResponse<Object> response =  CustomResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Email 400 not verified
    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<CustomResponse<Object>> handlerEmailNotVerified(EmailNotVerifiedException ex, HttpServletRequest request) {
       CustomResponse <Object> response =CustomResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EmailAlreadyVerifiedException.class)
    public ResponseEntity<CustomResponse<Object>> handlerEmailAlreadyVerified(EmailAlreadyVerifiedException ex, HttpServletRequest request) {
       CustomResponse <Object> response =CustomResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //400 - invalid verification code

    @ExceptionHandler(InvalidVerificationException.class)
    public ResponseEntity<CustomResponse<Object>> handlerInvalidVerification(InvalidVerificationException ex, HttpServletRequest request) {
      CustomResponse <Object> response = CustomResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    //400 - verification code expired

    @ExceptionHandler(VerificationCodeExpiredException.class)
    public ResponseEntity<CustomResponse<Object>> handlerVerificationCodeExpired(VerificationCodeExpiredException ex, HttpServletRequest request) {
            CustomResponse<Object> response = CustomResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    //409 -user Already exist
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<CustomResponse<Object>> handlerUserAlreadyExist(UserAlreadyExistException ex, HttpServletRequest request) {
      CustomResponse<Object> response =  CustomResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    //500  email send failed
    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<CustomResponse<Object>> handleEmailSendFail(EmailSendException ex, HttpServletRequest request) {
        CustomResponse<Object> response = CustomResponse.builder()
                .success(false)
                .message("Failed to send email. Please try again later.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // 401 bad credential
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomResponse<Object>> handleBadCredential(BadCredentialsException ex, HttpServletRequest request) {
        CustomResponse<Object> response = CustomResponse.builder()
                .success(false)
                .message("Invalid username or password")
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // 404 username not found
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleUsernameNotFound(UsernameNotFoundException ex, HttpServletRequest request) {
        CustomResponse<Object> response = CustomResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleGenericException(Exception ex, HttpServletRequest request) {

        System.err.println("Unexpected error: " + ex.getMessage());
        ex.printStackTrace();


        CustomResponse<Object> response = CustomResponse.builder()
                .success(false)
                .message("An unexpected error occurred.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentConversionNotSupportedException.class)
    public ResponseEntity<CustomResponse<Object>> handleMethodArgumentConversionNotSupportedException(MethodArgumentConversionNotSupportedException ex, HttpServletRequest request) {
        CustomResponse<Object> response = CustomResponse.builder()
                .success(false)
                .message("Invalid input type provided.")
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<CustomResponse<Object>> handleRefreshTokenExpiredException(RefreshTokenExpiredException ex, HttpServletRequest request) {
        CustomResponse<Object> response = CustomResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException ex, HttpServletRequest request) {
        CustomResponse<Object> response = CustomResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }






}
