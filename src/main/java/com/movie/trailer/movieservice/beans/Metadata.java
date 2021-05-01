package com.movie.trailer.movieservice.beans;

import lombok.Data;

import java.io.Serializable;

@Data
public class Metadata implements Serializable {
    private int page;
    private int totalTrailers;
    private int pageSize;
}
