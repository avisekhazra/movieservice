package com.movie.trailer.movieservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {

    private String id;
    @Getter @Setter
    private String title;
    @Getter @Setter
    private String message;

    @Getter @Setter
    private String source;
    public ExceptionResponse(String title, String message) {
        this.title = title;
        this.message = message;
    }
    public ExceptionResponse(String title, String message, String source) {
        this.title = title;
        this.message = message;
        this.source = source;
    }

    public String getId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


}
