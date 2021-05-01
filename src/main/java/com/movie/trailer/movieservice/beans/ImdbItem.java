package com.movie.trailer.movieservice.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImdbItem {

    @JsonProperty("Title")
    private String Title;

    @JsonProperty("Year")
    private String Year;

    private String imdbID;

    @JsonProperty("Type")
    private String Type;

    @JsonProperty("Poster")
    private String Poster;
}
