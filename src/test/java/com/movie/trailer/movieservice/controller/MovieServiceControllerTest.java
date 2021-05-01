package com.movie.trailer.movieservice.controller;

import com.movie.trailer.movieservice.beans.Metadata;
import com.movie.trailer.movieservice.beans.MovieTrailerBean;
import com.movie.trailer.movieservice.beans.RequestBean;
import com.movie.trailer.movieservice.beans.ResponseBean;
import com.movie.trailer.movieservice.service.IMovieTrailerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MovieServiceControllerTest {

    @MockBean
    private IMovieTrailerService movieTrailerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /trailers Test - Success")
    public void getTrailersSuccess() throws Exception {

        //given
        var req = new RequestBean("title", "2016","NL", "en",0);
        var trailer = new MovieTrailerBean();
        trailer.setName("title");
        trailer.setYear("2018");
        var trailerList = new ArrayList<MovieTrailerBean>();
        trailerList.add(trailer);
        var res = new ResponseBean(true,trailerList,new Metadata());
        given(movieTrailerService.findMovieTrailers(req)).willReturn(Optional.ofNullable(res));
        //when


        mockMvc.perform(get("/trailers")
                           .param("query","title")
                           .param("country", "NL")
                           .param("language","en")
                           .param("year", "2016"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));


    }

    @Test
    @DisplayName("GET /trailers Test - failure missing country")
    public void getTrailersFailureMissingCountry() throws Exception {

        //given
        var req = new RequestBean("title", "2016","NL", "en",0);
        var trailer = new MovieTrailerBean();
        trailer.setName("title");
        trailer.setYear("2018");
        var trailerList = new ArrayList<MovieTrailerBean>();
        trailerList.add(trailer);
        var res = new ResponseBean(true,trailerList,new Metadata());
        given(movieTrailerService.findMovieTrailers(req)).willReturn(Optional.ofNullable(res));
        //when


        mockMvc.perform(get("/trailers")
                .param("query","title")
                .param("language","en")
                .param("year", "2016"))

                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required Parameter is missing"));


    }

    @Test
    @DisplayName("GET /trailers Test - failure Invalid language")
    public void getTrailersFailureInvalidLanguage() throws Exception {

        //given
        var req = new RequestBean("title", "2016","NL", "en",0);
        var trailer = new MovieTrailerBean();
        trailer.setName("title");
        trailer.setYear("2018");
        var trailerList = new ArrayList<MovieTrailerBean>();
        trailerList.add(trailer);
        var res = new ResponseBean(true,trailerList,new Metadata());
        given(movieTrailerService.findMovieTrailers(req)).willReturn(Optional.ofNullable(res));
        //when


        mockMvc.perform(get("/trailers")
                .param("query","title")
                .param("language","eng")
                .param("country", "NL")
                .param("year", "2016"))

                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"));


    }
}
