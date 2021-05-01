package com.movie.trailer.movieservice.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class Trailers implements Serializable {

    private List<MovieTrailerBean> trailers;
    private Integer totalTrailers;

}
