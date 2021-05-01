package com.movie.trailer.movieservice.service;

import com.movie.trailer.movieservice.beans.MovieTrailerBean;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface IYoutubeService {
    List<MovieTrailerBean> getYouTubetrailers(List<MovieTrailerBean> list, String country, String language) throws IOException, InterruptedException, ExecutionException, TimeoutException;
}
