package com.grabit.Utilities;

import com.grabit.bean.member.MemberDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    private static final String SECRET="my-current-secret-key-for-verifying-member-token";

    private static final Long EXPIRATION_TIME= (long) (1000*60*60);

    private static final SecretKey SECRET_KEY= Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String generateTokenForEndUser(MemberDTO memberDTO,Long roleId){
        Map<String,Object> claims=new HashMap<>();
        claims.put("role",roleId);
        claims.put("member_id",memberDTO.getId());
        claims.put("access_level","member");
        long now = System.currentTimeMillis();
        Date iat = new Date(now);
        Date exp = new Date(now + EXPIRATION_TIME);
        return Jwts.builder()
                .subject(memberDTO.getEmail())
                .claims(claims)
                .issuedAt(iat)
                .expiration(exp)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String generateTokenForAdmin(String email,Long roleId){
        Map<String,Object> claims=new HashMap<>();
        claims.put("role",roleId);
        claims.put("access_level","admin");
        long now = System.currentTimeMillis();
        Date iat = new Date(now);
        Date exp = new Date(now + EXPIRATION_TIME);
        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .issuedAt(iat)
                .expiration(exp)
                .signWith(SECRET_KEY)
                .compact();
    }
}
