package com.movie.trailer.movieservice.configurations;


import com.movie.trailer.movieservice.beans.MovieTrailerBean;
import com.movie.trailer.movieservice.beans.ResponseBean;
import com.movie.trailer.movieservice.beans.Trailers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;

@Configuration
public class RedisConfigurations extends CachingConfigurerSupport {

    @Autowired
    private MovieServiceConfiguration movieServiceConfiguration;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        var host = movieServiceConfiguration.getRedis().getHost();
        var port = movieServiceConfiguration.getRedis().getPort();

        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    private RedisCacheConfiguration createCacheConfiguration(long timeoutInSeconds) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(timeoutInSeconds));
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return createCacheConfiguration(movieServiceConfiguration.getRedis().getTtl());
    }

    @Bean
    public RedisTemplate<String, ResponseBean> redisTemplate(@Qualifier("lettuceConnectionFactory") RedisConnectionFactory cf) {
        var redisTemplate = new RedisTemplate<String, ResponseBean>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
    }
}
