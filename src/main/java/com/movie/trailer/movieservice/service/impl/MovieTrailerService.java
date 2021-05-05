package com.movie.trailer.movieservice.service.impl;

import com.movie.trailer.movieservice.beans.*;
import com.movie.trailer.movieservice.configurations.MovieServiceConfiguration;
import com.movie.trailer.movieservice.exception.beans.TrailersNotFoundException;
import com.movie.trailer.movieservice.service.IImdbService;
import com.movie.trailer.movieservice.service.IMovieTrailerService;
import com.movie.trailer.movieservice.service.IYoutubeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MovieTrailerService implements IMovieTrailerService {
    @Autowired
    private IImdbService imdbService;

    @Autowired
    private IYoutubeService youtubeService;

    @Autowired
    private MovieServiceConfiguration movieServiceConfiguration;

    private static final String CONTROLLED_PREFIX = "movietrailer111_";

    public static String getCacheKey(RequestBean bean){
        return CONTROLLED_PREFIX + URLEncoder.encode(bean.getQuery(),StandardCharsets.UTF_8)
                + "_" + bean.getYear() + "_" + bean.getPage()
                + "_" + bean.getCountry() + "_" + bean.getLanguage();
    }

    @Cacheable(cacheNames = "movieServiceCacheNew", key = "T(com.movie.trailer.movieservice.service.impl.MovieTrailerService).getCacheKey(#bean)")
    public Optional<ResponseBean> findMovieTrailers(RequestBean bean){
        ResponseBean res = null;
        try{
            var movieList = imdbService.findMoviesFromImdb(bean);
            List<MovieTrailerBean> finalList = movieList.stream().map(m->{
                var b = new MovieTrailerBean();
                b.setName(m.getTitle());
                b.setYear(m.getYear());
                return b;
            }).collect(Collectors.toList());

            var list = youtubeService.getYouTubetrailers(finalList, bean.getCountry(), bean.getLanguage());
            if(list.size()>0)
                res = prepareResponse(new Trailers(list, list.size()),bean);

        }catch (Exception ex){
            log.error("Exception occurred in findMovieTrailers {}", ex.getMessage());
        }

        return Optional.ofNullable(res);
    }

    private ResponseBean prepareResponse(Trailers totalTrailers, RequestBean bean) throws TrailersNotFoundException {

        if (totalTrailers ==null)
            return null;
        var trailers = totalTrailers.getTrailers();
        var meta = new Metadata();
        int pageSize = movieServiceConfiguration.getPageSize();
        meta.setPageSize(pageSize);
        meta.setTotalTrailers(trailers.size());
        int startIndex = 0;
        int endIndex = 0;
        if(bean.getPage()<=1){
           meta.setPage(1);
           startIndex = 0;
            endIndex = trailers.size()<pageSize ?  trailers.size(): pageSize;
        }else{

            startIndex = (bean.getPage()-1) * pageSize -1;
            endIndex = startIndex + pageSize;
            if(startIndex>trailers.size()-1)
                throw new TrailersNotFoundException("No trailers found", "page");
            meta.setPage(bean.getPage());
            endIndex = endIndex > trailers.size()-1 ? trailers.size()-1: endIndex;

        }
        var finalTrailers = new ArrayList<>(trailers.subList(startIndex,endIndex));

        return  new ResponseBean(true,finalTrailers,meta);
    }
}
