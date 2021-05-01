package com.movie.trailer.movieservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HttpClientUtils {

    public  HttpResponse<String> getApiRequest(String apiUrl,
                                                     Map<String,String> singleValueHeaders,
                                                     long timeOutInMils)
                                                     throws URISyntaxException, IOException, InterruptedException
    {

        HttpRequest.Builder builder = HttpRequest.newBuilder();
        singleValueHeaders.forEach(builder::header);

        HttpRequest request = builder
                .uri(new URI(apiUrl))
                .timeout(Duration.ofMillis(timeOutInMils))
                .GET()
                .build();
        return HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

    }
}
