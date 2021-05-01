package com.movie.trailer.movieservice.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.services.youtube.model.ThumbnailDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class MovieTrailerBean implements Serializable {

    @JsonProperty("movieName")
    private String name;
    private String year;
    private String poster;
    private String trailerName;
    private String trailer;
    private Map<String,Thumbnails> thumbnails;
}
