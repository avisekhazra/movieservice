package com.movie.trailer.movieservice.service;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.movie.trailer.movieservice.beans.MovieTrailerBean;
import com.movie.trailer.movieservice.service.impl.YoutubeService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class YoutubeServiceTest {

    @SpyBean
    private YoutubeService youtubeService;

    @MockBean
    private IMovieTrailerService iMovieTrailerService;

    @Test
    public void testFindYouTubeTrailersSuccess() throws IOException, TimeoutException, InterruptedException {
        //given

        var searchResult = new SearchResult();
        var resource = new ResourceId();
        resource.setVideoId("122");
        resource.setKind("movie");
        searchResult.setId(resource);
        searchResult.setKind("video");
        var snippet = new SearchResultSnippet();
        snippet.setTitle("title trailer");
        searchResult.setSnippet(snippet);
        var list = new ArrayList<SearchResult>();
        list.add(searchResult);
        given(youtubeService.findYouTubeTrailers(any(),eq("NL"),eq("en"))).willReturn(list);
        var trailer = new MovieTrailerBean();
        trailer.setName("title");
        trailer.setYear("2018");
        var trailerList = new ArrayList<MovieTrailerBean>();
        trailerList.add(trailer);

        //when

        var trailers = youtubeService.getYouTubetrailers(trailerList,"NL","en");

        //then

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(trailers.size())
                    .as("Check the Items size")
                    .isEqualTo(list.size());
            softly.assertThat(trailers.get(0).getTrailer())
                    .as("Check the First Item")
                    .isEqualTo(resource.getVideoId());
        });
    }
}
