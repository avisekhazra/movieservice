package com.movie.trailer.movieservice.service;

import com.google.api.services.youtube.model.*;
import com.movie.trailer.movieservice.beans.MovieTrailerBean;
import com.movie.trailer.movieservice.service.impl.YoutubeService;
import com.movie.trailer.movieservice.utils.YoutubeUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private YoutubeService youtubeService;

    @MockBean
    private YoutubeUtils youtubeUtils;

    @Test
    @DisplayName("Test - success secnario with default thumbnail")
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
        var t = new Thumbnail();
        t.setHeight(80l);
        t.setWidth(40l);
        t.setUrl("url");
        var ts = new ThumbnailDetails();
        ts.setDefault(t);
        snippet.setThumbnails(ts);
        searchResult.setSnippet(snippet);
        var list = new ArrayList<SearchResult>();
        list.add(searchResult);
        given(youtubeUtils.findYouTubeTrailers(any(),eq("NL"),eq("en"))).willReturn(list);
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
            softly.assertThat(trailers.get(0).getThumbnails().get("default").getUrl())
                    .as("Check the default thumbnail Item")
                    .isEqualTo("url");
        });
    }

    @Test
    @DisplayName("Test - success secnario with other thumbnails")
    public void testFindYouTubeTrailersThumbnailsSuccess() throws IOException, TimeoutException, InterruptedException {
        //given

        var searchResult = new SearchResult();
        var resource = new ResourceId();
        resource.setVideoId("122");
        resource.setKind("movie");
        searchResult.setId(resource);
        searchResult.setKind("video");
        var snippet = new SearchResultSnippet();
        snippet.setTitle("title trailer");
        var ts = new ThumbnailDetails();
        var t = new Thumbnail();
        t.setHeight(80l);
        t.setWidth(40l);
        t.setUrl("url");
        ts.setMedium(t);
        var tHigh = new Thumbnail();
        tHigh.setHeight(80l);
        tHigh.setWidth(40l);
        tHigh.setUrl("highUrl");
        ts.setHigh(tHigh);
        snippet.setThumbnails(ts);
        searchResult.setSnippet(snippet);
        var list = new ArrayList<SearchResult>();
        list.add(searchResult);
        given(youtubeUtils.findYouTubeTrailers(any(),eq("NL"),eq("en"))).willReturn(list);
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
            softly.assertThat(trailers.get(0).getThumbnails().get("medium").getUrl())
                    .as("Check the medium thumbnail Item")
                    .isEqualTo("url");
            softly.assertThat(trailers.get(0).getThumbnails().get("high").getUrl())
                    .as("Check the high resolution thumbnail Item")
                    .isEqualTo("highUrl");
        });
    }
}
