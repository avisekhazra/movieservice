package com.movie.trailer.movieservice.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Thumbnails implements Serializable {
    private Long height;
    private String url;
    private Long width;
}
