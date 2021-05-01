package com.movie.trailer.movieservice.service;

import com.movie.trailer.movieservice.beans.ImdbBean;
import com.movie.trailer.movieservice.beans.ImdbItem;
import com.movie.trailer.movieservice.beans.RequestBean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface IImdbService {
    List<ImdbItem> findMoviesFromImdb(RequestBean req) throws InterruptedException, IOException, URISyntaxException;
    ImdbBean findMoviesImdbByPage(String query, String year, int page) throws InterruptedException, IOException, URISyntaxException;
}
