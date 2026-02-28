package com.grabit.helper.auth;

import com.grabit.entity.Role;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

@Log4j2
public class RedisHelper {

    private RedisTemplate<String,Object> redisTemplate;

    public RedisHelper(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addOrUpdateRoleDetailsToRedis(Role role,String cacheName){
        try{
            String key=cacheName+"_"+role.getId();
            redisTemplate.opsForValue().set(key,role);
            log.info("Role added/updated to Redis : "+role.getId());
            printRedisContents(cacheName);
        } catch (Exception e){
            log.error("Error while adding/updating role in redis : "+e);
        }
    }

    public void printRedisContents(String cacheName){
        try{
            Set<String> keys=redisTemplate.keys(cacheName+"*");
            for(String key:keys){
                System.out.println(redisTemplate.opsForValue().get(key));
            }
        } catch (Exception e){
            log.error("Error while printing role details from redis : "+e);
        }
    }
}
