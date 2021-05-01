package com.movie.trailer.movieservice.exception.beans;

import lombok.Getter;
import lombok.Setter;

public class TrailersNotFoundException extends Exception {
    @Getter @Setter
    private String code;
    public TrailersNotFoundException(){
        super();
    }
    public TrailersNotFoundException(String message, String code){
        super(message);
        this.code = code;
    }
}
