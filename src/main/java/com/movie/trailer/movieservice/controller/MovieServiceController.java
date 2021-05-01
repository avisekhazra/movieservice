package com.movie.trailer.movieservice.controller;

import com.movie.trailer.movieservice.beans.RequestBean;
import com.movie.trailer.movieservice.service.IImdbService;
import com.movie.trailer.movieservice.service.IMovieTrailerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MovieServiceController {


    @Autowired
    private IMovieTrailerService movieTrailerService;


    @GetMapping("/trailers")
    public ResponseEntity<?> searchMovieTrailers(@ModelAttribute RequestBean bean){
        return movieTrailerService.findMovieTrailers(bean)
                .map(event -> ResponseEntity
                    .ok()
                    .body(event)
        ).orElse(ResponseEntity.notFound().build());

    }
}
