package com.movie.trailer.movieservice.service.impl;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Thumbnail;
import com.movie.trailer.movieservice.beans.MovieTrailerBean;
import com.movie.trailer.movieservice.beans.Thumbnails;
import com.movie.trailer.movieservice.configurations.MovieServiceConfiguration;
import com.movie.trailer.movieservice.service.IYoutubeService;
import com.movie.trailer.movieservice.utils.YoutubeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class YoutubeService implements IYoutubeService {


    @Autowired
    private MovieServiceConfiguration movieServiceConfiguration;

    @Autowired
    private YoutubeUtils youtubeUtils;

    public List<MovieTrailerBean> getYouTubetrailers(List<MovieTrailerBean> trailerList, String country, String language) throws InterruptedException, TimeoutException {

        int threads = movieServiceConfiguration.getYouTube().getThreads();
        var finalList = new ArrayList<MovieTrailerBean>();
        int noOfThreads = trailerList.size() > threads ? threads : trailerList.size();
        ExecutorService executorService =  Executors.newFixedThreadPool(noOfThreads);
        var futures = new ArrayList<Future<List<MovieTrailerBean>>>();
        for(MovieTrailerBean m: trailerList){
            futures.add(executorService.submit(
                    () -> getTrailers(m, country, language)
            ));
        }

        for(Future<List<MovieTrailerBean>> f: futures){
            try{
                finalList.addAll(f.get(movieServiceConfiguration.getYouTube().getTimeout(), TimeUnit.MILLISECONDS));
            }catch (ExecutionException ex){
                log.error(ex.getMessage());
                // to be handled specific exceptions
                break;
            }

        }
        log.info(finalList.toString());
        executorService.shutdown();
        return finalList;
    }

    private List<MovieTrailerBean> getTrailers(MovieTrailerBean movieTrailerBean, String country, String language) throws IOException {

        var trailerList = new ArrayList<MovieTrailerBean>();
        trailerList.addAll(filterTrailers(movieTrailerBean, youtubeUtils.findYouTubeTrailers(movieTrailerBean.getName(), country, language)));
        return trailerList;
    }

    private ArrayList<MovieTrailerBean> filterTrailers(MovieTrailerBean movieTrailerBean, List<SearchResult> searchResultList) {
        var localList = new ArrayList<MovieTrailerBean>();
        for(SearchResult s: searchResultList){
            if(s.getSnippet()!=null && s.getSnippet().getTitle().toLowerCase().contains(movieTrailerBean.getName().toLowerCase())&&
               s.getSnippet().getTitle().toLowerCase().contains("trailer"))
            {
                var movieTrailer = new MovieTrailerBean();
                movieTrailer.setName(movieTrailerBean.getName());
                movieTrailer.setYear(movieTrailerBean.getYear());
                movieTrailer.setTrailer(s.getId().getVideoId());
                movieTrailer.setThumbnails(populateThumnails(s.getSnippet()));
                movieTrailer.setTrailerName(s.getSnippet().getTitle());

                localList.add(movieTrailer);
            }

        }
        return localList;
    }

    private Map<String,Thumbnails> populateThumnails(SearchResultSnippet s){
        var thumbnails = new HashMap<String , Thumbnails>();

        if(s.getThumbnails()==null)
            return thumbnails;
        var t = s.getThumbnails();
        if(t.getDefault()!=null){
            thumbnails.put("default", getEachThumbnail(t.getDefault()));
        }
        if(t.getHigh()!=null){
            thumbnails.put("high", getEachThumbnail(t.getHigh()));
        }
        if(t.getMedium()!=null){
            thumbnails.put("medium", getEachThumbnail(t.getMedium()));
        }

        return thumbnails;
    }

    private Thumbnails getEachThumbnail(Thumbnail t){
        return new Thumbnails(t.getHeight(),t.getUrl(),t.getWidth());
    }


}
