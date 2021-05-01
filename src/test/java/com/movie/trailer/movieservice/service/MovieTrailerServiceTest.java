package com.movie.trailer.movieservice.service;
import com.movie.trailer.movieservice.beans.ImdbItem;
import com.movie.trailer.movieservice.beans.MovieTrailerBean;
import com.movie.trailer.movieservice.beans.RequestBean;
import com.movie.trailer.movieservice.configurations.MovieServiceConfiguration;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class MovieTrailerServiceTest {

    @MockBean
    private IImdbService imdbService;

    @MockBean
    private IYoutubeService youtubeService;

    @Autowired
    private IMovieTrailerService movieTrailerService;

    @Autowired
    MovieServiceConfiguration movieServiceConfiguration;


    @Test
    @DisplayName("Test case - Find movie trailers success scenario with 2 pages")
    public void testFindMovieTrailersSuccess() throws InterruptedException, IOException, URISyntaxException, TimeoutException, ExecutionException {

        //given
        var imdbTestItem = new ImdbItem("title", "2018", "id", null, null);
        var otherImdbTestItem = new ImdbItem("other", "2018", "id", null, null);
        var listImdbTestItem = new ArrayList<ImdbItem>();
        listImdbTestItem.add(imdbTestItem);
        listImdbTestItem.add(otherImdbTestItem);
        var req = new RequestBean("title", "2018","NL", "en",1);
        given(imdbService.findMoviesFromImdb(req)).willReturn(listImdbTestItem);
        var trailer = new MovieTrailerBean("title", "2018", "poster", "trailer", "trailerName",null);
        var othertrailer = new MovieTrailerBean("other", "2018", "poster", "trailer", "trailerName",null);
        var movieTrailersList = new ArrayList<MovieTrailerBean>();
        movieTrailersList.add(trailer);
        movieTrailersList.add(othertrailer);
        given(youtubeService.getYouTubetrailers(any(), any(), any())).willReturn(movieTrailersList);

        //when
         var response = movieTrailerService.findMovieTrailers(req);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.get().isSuccess())
                    .as("Check whether trailers are returned")
                    .isEqualTo(true);
            softly.assertThat(response.get().getData().size())
                    .as("Check number of trailers in the current page.")
                    .isEqualTo(movieServiceConfiguration.getPageSize());
            softly.assertThat(response.get().getMetaData().getTotalTrailers())
                    .as("Check total number of trailers.")
                    .isEqualTo(movieTrailersList.size());
            softly.assertThat(response.get().getData().size())
                    .as("Check the current page size is less than the total trailers")
                    .isLessThan(response.get().getMetaData().getTotalTrailers());
        });
    }

    @Test
    @DisplayName("Test case - Find movie trailers success scenario with 1 page")
    public void testFindMovieTrailersSingleSuccess() throws InterruptedException, IOException, URISyntaxException, TimeoutException, ExecutionException {

        //given
        var imdbTestItem = new ImdbItem("title", "2016", "id", null, null);
        var listImdbTestItem = new ArrayList<ImdbItem>();
        listImdbTestItem.add(imdbTestItem);
        var req = new RequestBean("title", "2016","NL", "en",1);
        given(imdbService.findMoviesFromImdb(req)).willReturn(listImdbTestItem);
        var trailer = new MovieTrailerBean("title", "2016", "poster", "trailer", "trailerName",null);
        var movieTrailersList = new ArrayList<MovieTrailerBean>();
        movieTrailersList.add(trailer);
        given(youtubeService.getYouTubetrailers(any(), any(), any())).willReturn(movieTrailersList);

        //when
        var response = movieTrailerService.findMovieTrailers(req);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.get().isSuccess())
                    .as("Check whether trailers are returned")
                    .isEqualTo(true);
            softly.assertThat(response.get().getData().size())
                    .as("Check number of trailers in the current page.")
                    .isEqualTo(movieTrailersList.size());
            softly.assertThat(response.get().getMetaData().getTotalTrailers())
                    .as("Check total number of trailers.")
                    .isEqualTo(movieTrailersList.size());
            softly.assertThat(response.get().getData().size())
                    .as("Check the current page size is less than the total trailers")
                    .isEqualTo(response.get().getMetaData().getTotalTrailers());
        });
    }

    @Test
    @DisplayName("Test case - Find movie trailers failure scenario without trailers.")
    public void testFindMovieTrailersFailure() throws InterruptedException, IOException, URISyntaxException, TimeoutException, ExecutionException {

        //given
        var imdbTestItem = new ImdbItem("title", "2015", "id", null, null);
        var listImdbTestItem = new ArrayList<ImdbItem>();
        listImdbTestItem.add(imdbTestItem);
        var req = new RequestBean("title", "2015","NL", "en",1);
        given(imdbService.findMoviesFromImdb(req)).willReturn(listImdbTestItem);
        given(youtubeService.getYouTubetrailers(any(), any(), any())).willReturn(new ArrayList<MovieTrailerBean>());

        //when
        var response = movieTrailerService.findMovieTrailers(req);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.isPresent())
                    .as("Check whether trailers are returned")
                    .isEqualTo(false);
        });
    }


}
