//package com.grabit.service;
//
//import com.grabit.Utilities.Utility;
//import com.grabit.bean.member.MemberDTO;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.CacheManager;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//@Service
//@Log4j2
//public class CacheService {
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
////    public void printCacheContent(String cacheName){
////        Cache cache=cacheManager.getCache(cacheName);
////        if(cache!=null){
////            System.out.println(Utility.toJson(cache.getNativeCache()));
////        }
////        else
////            System.out.println("No contents in the Cache");
////    }
//
//    public void printCacheContentFromRedis(String cacheName) {
//        Set<String> keys = redisTemplate.keys(cacheName + '*');
//        if (keys != null && !keys.isEmpty()) {
//            for (String key : keys) {
//                System.out.println(Utility.toJson(redisTemplate.opsForValue().get(key)));
//            }
//        } else
//            System.out.println("No contents in the Cache");
//    }
//
//    //    public void addMemberToCache(List<MemberDTO> memberList){
////        try {
////            Cache cache = cacheManager.getCache("member");
////            if (!Utility.isNullOrEmpty(cache)) {
////                for (MemberDTO memberDTO : memberList) {
////                    if(Utility.isNullOrEmpty(cache.get(memberDTO.getId())))
////                        cache.put(memberDTO.getId(), memberDTO);
////                }
////            }
////        } catch (Exception e){
////            log.info("Adding Member data to Cache is Unsuccessful");
////            log.info(e.getMessage());
////        }
////    }
//    public void addMemberToRedisCache(List<MemberDTO> memberList) {
//        try {
//            for (MemberDTO memberDTO : memberList) {
//                String key = "member::" + memberDTO.getId();
//                if (Utility.isNullOrEmpty(redisTemplate.opsForValue().get(key)))
//                    redisTemplate.opsForValue().set(key, memberDTO, 3600, TimeUnit.SECONDS);
//            }
//        } catch (Exception e) {
//            log.info("Adding Member data to Cache is Unsuccessful : " + e);
//        }
//    }
//}
