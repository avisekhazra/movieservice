package com.movie.trailer.movieservice.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "movieservice")
@Data
public class MovieServiceConfiguration {

    @Data
    public static class ImdbConfig{
        private String host;
        private String apiKey;
        private int timeout;
    }

    @Data
    public static class YoutubeConfig{
        private String apiKey;
        private int timeout;
        private int threads;
    }

    @Data
    public static class RedisConfig{
        private String host;
        private int port;
        private int ttl;
    }

    private ImdbConfig imdb;
    private YoutubeConfig youTube;
    private RedisConfig redis;
    private int pageSize;
}
