package com.movie.trailer.movieservice.exception;

import com.movie.trailer.movieservice.exception.beans.TrailersNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> processException(Exception ex){
        HttpHeaders responseHeaders = new HttpHeaders();
        ExceptionResponse exceptionResponse = new ExceptionResponse("INTERNAL_SERVER_ERROR", "Internal Server Error");
        return new ResponseEntity<>(exceptionResponse, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> processMethodNotSupportedException(){
        HttpHeaders responseHeaders = new HttpHeaders();
        ExceptionResponse exceptionResponse = new ExceptionResponse("METHOD_NOT_ALLOWED", "Requested Method not allowed.");
        return new ResponseEntity<>(exceptionResponse, responseHeaders, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> processHandlerNotFoundException(){
        HttpHeaders responseHeaders = new HttpHeaders();
        ExceptionResponse exceptionResponse = new ExceptionResponse("NO_HANDLER_FOUND", "Requested API url does not point to the valid REST API.");
        return new ResponseEntity<>(exceptionResponse, responseHeaders, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> processMissingRequestParamException(MissingServletRequestParameterException ex){
        HttpHeaders responseHeaders = new HttpHeaders();
        ExceptionResponse exceptionResponse = new ExceptionResponse("BAD_REQUEST", "Required Parameter is missing", ex.getParameterName());
        return new ResponseEntity<>(exceptionResponse, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TrailersNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> processTrailerException(TrailersNotFoundException ex){
        HttpHeaders responseHeaders = new HttpHeaders();
        ExceptionResponse exceptionResponse = new ExceptionResponse("BAD_REQUEST", ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(exceptionResponse, responseHeaders, HttpStatus.BAD_REQUEST);
    }
}
