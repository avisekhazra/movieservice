package com.movie.trailer.movieservice.service;

import com.movie.trailer.movieservice.beans.ImdbBean;
import com.movie.trailer.movieservice.beans.ImdbItem;
import com.movie.trailer.movieservice.beans.RequestBean;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ImdbServiceTest {

    @SpyBean
    private IImdbService imdbService;

    @Test
    @DisplayName("Test case - Find Movie from IMDB with page = 2")
    public void testFindMoviesFromImdbRecursionSuccess() throws InterruptedException, IOException, URISyntaxException {
        //given
        var imdbTestItem = new ImdbItem("title", "2018", "id", null, null);
        var otherImdbTestItem = new ImdbItem("other", "2018", "id", null, null);
        var listImdbTestItem = new ArrayList<ImdbItem>();
        listImdbTestItem.add(imdbTestItem);
        listImdbTestItem.add(otherImdbTestItem);
        var imdbBean = new ImdbBean(listImdbTestItem, "2", "True");
        given(imdbService.findMoviesImdbByPage(any(),eq("2018"), eq(1))).willReturn(imdbBean);
        var req = new RequestBean("title", "2018","NL", "en",1);
        //when

        var imdbItems = imdbService.findMoviesFromImdb(req);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(imdbItems.size())
                    .as("Check the Items size")
                    .isEqualTo(2);
            softly.assertThat(imdbItems.get(0).getTitle())
                    .as("Check the First Item")
                    .isEqualTo(imdbTestItem.getTitle());
        });

    }

    @Test
    @DisplayName("Test case - Find Movie from IMDB with page = 1")
    public void testFindMoviesFromImdbSinglePageSuccess() throws InterruptedException, IOException, URISyntaxException {
        //given
        var imdbTestItem = new ImdbItem("title", "2018", "id", null, null);
        var listImdbTestItem = new ArrayList<ImdbItem>();
        listImdbTestItem.add(imdbTestItem);
        var imdbBean = new ImdbBean(listImdbTestItem, "1", "True");
        given(imdbService.findMoviesImdbByPage(any(),eq("2018"), eq(1))).willReturn(imdbBean);
        var req = new RequestBean("title", "2018","NL", "en",1);

        //when

        var imdbItems = imdbService.findMoviesFromImdb(req);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(imdbItems.size())
                    .as("Check the Items size")
                    .isEqualTo(1);
            softly.assertThat(imdbItems.get(0).getTitle())
                    .as("Check the First Item")
                    .isEqualTo(imdbTestItem.getTitle());
        });

    }

    @Test
    @DisplayName("Test case - Find Movie from IMDB failure case")
    public void testFindMoviesFromImdbfailure() throws InterruptedException, IOException, URISyntaxException {
        //given
        var imdbBean = new ImdbBean(null, "0", "False");
        given(imdbService.findMoviesImdbByPage(any(),eq("2018"), eq(1))).willReturn(imdbBean);
        var req = new RequestBean("title", "2018","NL", "en",1);

        //when

        var imdbItems = imdbService.findMoviesFromImdb(req);

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(imdbItems.size())
                    .as("Check the Items size")
                    .isEqualTo(0);
        });

    }
}
