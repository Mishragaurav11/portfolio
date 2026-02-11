package com.myportfolio.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimitService {

    private final Map<String, UserRequestInfo> requestMap = new HashMap<>();

    private static final int MAX_REQUEST = 3;
    private static final long TIME_WINDOW = 60; // seconds

    public boolean allowRequest(String key) {

        Instant now = Instant.now();

        UserRequestInfo info = requestMap.getOrDefault(key,
                new UserRequestInfo(0, now));

        if (now.getEpochSecond() - info.firstRequestTime.getEpochSecond() > TIME_WINDOW) {
            info.requestCount = 0;
            info.firstRequestTime = now;
        }

        info.requestCount++;

        requestMap.put(key, info);

        return info.requestCount <= MAX_REQUEST;
    }

    static class UserRequestInfo {
        int requestCount;
        Instant firstRequestTime;

        UserRequestInfo(int requestCount, Instant firstRequestTime) {
            this.requestCount = requestCount;
            this.firstRequestTime = firstRequestTime;
        }
    }
}
