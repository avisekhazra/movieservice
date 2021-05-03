package com.movie.trailer.movieservice.utils;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.movie.trailer.movieservice.configurations.MovieServiceConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class YoutubeUtils {

    @Autowired
    private MovieServiceConfiguration movieServiceConfiguration;
    public List<SearchResult> findYouTubeTrailers(String name, String country, String language) throws IOException {
        var youTube = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), httpRequest -> {

        }).setApplicationName("movie-trailer").build();
        YouTube.Search.List search = youTube.search().list(Collections.singletonList("id,snippet"));
        search.setKey(movieServiceConfiguration.getYouTube().getApiKey());
        search.setQ(name + " trailer");
        search.setType(Collections.singletonList("video"));
        search.setVideoType("any");
        search.setRegionCode(country);
        search.setRelevanceLanguage(language);
        search.setVideoDuration("short");
        search.setMaxResults(25l);

        var searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        return searchResultList;
    }


}
