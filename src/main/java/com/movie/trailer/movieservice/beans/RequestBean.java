package com.movie.trailer.movieservice.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBean {
    private String query;
    private String year;
    private String country;
    private String language;
    private Integer page;

}
