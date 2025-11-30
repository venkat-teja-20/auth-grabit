package com.grabit.api.webhook;

import com.grabit.repository.RoleRepository;
import com.grabit.service.webhook.CacheService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/webhook")
public class CacheRolesAndPermissions {

    private final CacheService cacheService;

    public CacheRolesAndPermissions(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping(value = "/cache/redis/refresh",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map> refreshRedis(){
        return cacheService.addRolesAndPermissionDetailsToRedis();
    }
}
