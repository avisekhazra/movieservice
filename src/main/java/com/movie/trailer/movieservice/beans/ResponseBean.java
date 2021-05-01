package com.movie.trailer.movieservice.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ResponseBean implements Serializable {
    private boolean success;
    private List<MovieTrailerBean> data;
    private Metadata metaData;
}
