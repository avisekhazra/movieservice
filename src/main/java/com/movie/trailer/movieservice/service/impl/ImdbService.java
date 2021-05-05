package com.movie.trailer.movieservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.trailer.movieservice.beans.ImdbBean;
import com.movie.trailer.movieservice.beans.ImdbItem;
import com.movie.trailer.movieservice.beans.RequestBean;
import com.movie.trailer.movieservice.service.IImdbService;
import com.movie.trailer.movieservice.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ImdbService implements IImdbService {

    @Autowired
    private HttpClientUtils httpClientUtils;

    @Value("${imdb.rapidApi.host}")
    private String host;

    @Value("${imdb.rapidApi.apiKey}")
    private String apiKey;

    @Value("${imdb.rapidApi.timeout:5000}")
    private long timeout;

    @Value("${imdb.rapidApi.maxTrailers:100}")
    private long maxTrailers;

    private static final int PAGE_SIZE = 10;

    @Override
    public List<ImdbItem> findMoviesFromImdb(RequestBean req) throws InterruptedException, IOException, URISyntaxException {

            String query= URLEncoder.encode(req.getQuery(), StandardCharsets.UTF_8.toString());
            int page = 1;
            boolean recursion = true;
            var imdbData = new ArrayList<ImdbItem>();
            do {
                var pagedResults = findMoviesImdbByPage(query,req.getYear(),page);
                if ("True".equalsIgnoreCase(pagedResults.getResponse())){
                    imdbData.addAll(pagedResults.getSearch());
                    if(pagedResults.getSearch().size()==PAGE_SIZE && imdbData.size() < maxTrailers) {
                        page++;
                    } else recursion = false;
                } else recursion = false;

            } while(recursion);


        return imdbData;
    }

    @Override
    public ImdbBean findMoviesImdbByPage(String query, String year, int page) throws InterruptedException, IOException, URISyntaxException {
        var headers = new HashMap<String ,String >();
        headers.put("x-rapidapi-key",apiKey);
        headers.put("x-rapidapi-host",host);
        var sb = new StringBuilder();
        sb.append("https://")
                .append(host)
                .append("?s=")
                .append(query)
                .append("&page=")
                .append(page)
                .append("&type=movie&r=json");
        if(year!=null && !year.isEmpty()) sb.append("&y=").append(year);
        var response= httpClientUtils.getApiRequest(sb.toString(),headers,timeout);
        log.info(response.body());
        ObjectMapper objectMapper = new ObjectMapper();
        var imdbdata = objectMapper.readValue(response.body(),ImdbBean.class);
        log.info(imdbdata.toString());


        return imdbdata;
    }



}
