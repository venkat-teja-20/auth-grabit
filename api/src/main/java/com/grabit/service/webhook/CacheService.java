package com.grabit.service.webhook;

import com.grabit.Utilities.ModelMapperUtility;
import com.grabit.Utilities.Utility;
import com.grabit.bean.auth.RoleDTO;
import com.grabit.entity.Role;
import com.grabit.exception.CustomException;
import com.grabit.repository.RoleRepository;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    private final RoleRepository roleRepository;

    private final CacheManager cacheManager;

    private final RedisTemplate<String, Object> redisTemplate;

    public CacheService(RoleRepository roleRepository, CacheManager cacheManager, RedisTemplate<String, Object> redisTemplate) {
        this.roleRepository = roleRepository;
        this.cacheManager = cacheManager;
        this.redisTemplate = redisTemplate;
    }

    public ResponseEntity<Map> addRolesAndPermissionDetailsToRedis() {
        List<Role> rolesAndPermissions = roleRepository.findAll();
        List<RoleDTO> roleDTOList = rolesAndPermissions.stream().map(roleAndPermission -> ModelMapperUtility.map(roleAndPermission, RoleDTO.class)).toList();
        for (RoleDTO roleAndPermission : roleDTOList) {
            String key = "role_" + roleAndPermission.getId();
            // read ttl from properties
            redisTemplate.opsForValue().set(key, roleAndPermission, 30, TimeUnit.DAYS);
        }
        return ResponseEntity.ok().body(Map.of("status", "SUCCESS"));
    }

    public ResponseEntity<Map<String, Object>> getRedisContents(String cacheName){
        try {
            Set<String> keys = redisTemplate.keys(cacheName + "*");
            if(keys.isEmpty())
                throw new CustomException(Utility.buildErrorObject("CACHE_NOT_FOUND","Cache name is not valid",404,"getRedisContents"));
            Map<String, Object> result = new HashMap<>();
            for (String key : keys) {
                result.put(key.split("_")[1], redisTemplate.opsForValue().get(key));
            }
            return ResponseEntity.ok().body(result);
        } catch (Exception e){
            if(e instanceof CustomException)
                throw (CustomException) e;
            throw new CustomException(Utility.buildErrorObject("SOMETHING_WENT_WRONG","Error while fetching details from Redis",500,"getRedisContents"));
        }
    }
}
