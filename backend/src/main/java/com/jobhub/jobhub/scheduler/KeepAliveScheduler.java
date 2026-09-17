package com.jobhub.jobhub.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class KeepAliveScheduler {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // Self-ping Render backend every 4 minutes (240,000 ms) to keep server awake 24/7 with ZERO sleep mode
    @Scheduled(fixedRate = 240000, initialDelay = 30000)
    public void keepAliveSelfPing() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://jobhub-backend-cigg.onrender.com/api/home/stats"))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            // Ignore background self-ping exceptions silently
        }
    }
}
