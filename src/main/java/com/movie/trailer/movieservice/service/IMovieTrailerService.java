package com.movie.trailer.movieservice.service;

import com.movie.trailer.movieservice.beans.MovieTrailerBean;
import com.movie.trailer.movieservice.beans.RequestBean;
import com.movie.trailer.movieservice.beans.ResponseBean;

import java.util.List;
import java.util.Optional;

public interface IMovieTrailerService {
    Optional<ResponseBean> findMovieTrailers(RequestBean rb);
}
