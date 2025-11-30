package com.grabit.api;

import com.grabit.service.webhook.CacheService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetCacheContents {

    private final CacheService cacheService;

    public GetCacheContents(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping(value = "/cache-details/{cacheName}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getRoleCacheDetails(@PathVariable(value = "cacheName") String cacheName){
        return cacheService.getRedisContents(cacheName);
    }
}
