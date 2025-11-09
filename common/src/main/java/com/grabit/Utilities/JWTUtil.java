package com.grabit.Utilities;

import com.grabit.bean.member.MemberDTO;
import com.grabit.config.KeyProvider;
import com.grabit.enums.CommonErrors;
import com.grabit.exception.APIError;
import com.grabit.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {

    private static final String SECRET="my-current-secret-key-for-verifying-member-token";

    private static PublicKey publicKey;

    private static PrivateKey privateKey;

    private static final Long EXPIRATION_TIME= (long) (1000*60*60);

    private static final Long REFRESH_EXPIRATION_TIME= (long) (1000*60*60*24*7);

    public JWTUtil(KeyProvider keyProvider){
        privateKey=keyProvider.getPrivateKey();
        publicKey=keyProvider.getPublicKey();
    }

    public static String generateTokenForEndUser(String email,Long id, Long roleId, List<Long> permissionIds,String accessLevel){
        Map<String,Object> claims=new HashMap<>();
        claims.put("role", roleId);
        claims.put("user_id", id);
        claims.put("permissions", permissionIds);
        claims.put("access_level",accessLevel.toLowerCase());
        long now = System.currentTimeMillis();
        Date iat = new Date(now);
        Date exp = new Date(now + EXPIRATION_TIME);
        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .issuedAt(iat)
                .expiration(exp)
                .signWith(privateKey,SignatureAlgorithm.RS256)
                .compact();
    }

    public static String generateTokenForEndUser(String email,String accessLevel){
        Map<String,Object> claims=new HashMap<>();
        claims.put("access_level",accessLevel.toLowerCase());
        long now = System.currentTimeMillis();
        Date iat = new Date(now);
        Date exp = new Date(now + EXPIRATION_TIME);
        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .issuedAt(iat)
                .expiration(exp)
                .signWith(privateKey,SignatureAlgorithm.RS256)
                .compact();
    }

    public static String generateTokenForAdmin(String email,Long roleId){
        Map<String,Object> claims=new HashMap<>();
        claims.put("role",roleId);
        claims.put("access_level","admin");
        long now = System.currentTimeMillis();
        Date iat = new Date(now);
        Date exp = new Date(now + REFRESH_EXPIRATION_TIME);
        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .issuedAt(iat)
                .expiration(exp)
                .signWith(privateKey,SignatureAlgorithm.RS256)
                .compact();
    }

    public static String getEmailFromRefreshToken(String refreshToken){
        return validateRefreshTokenAndGetEmail(refreshToken).getSubject();
    }

    private static Claims validateRefreshTokenAndGetEmail(String refreshToken){
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();
        } catch (SignatureException e){
            log.info("validateRefreshToken -> SignatureException");
            throw new JwtAuthenticationException(new APIError(CommonErrors.INVALID_REFRESH_TOKEN.toString(),CommonErrors.INVALID_REFRESH_TOKEN.getMessage()));
        }
        catch (MalformedJwtException e){
            log.info("validateRefreshToken -> MalformedJwtException");
            throw new JwtAuthenticationException(new APIError(CommonErrors.INVALID_REFRESH_TOKEN.toString(),CommonErrors.INVALID_REFRESH_TOKEN.getMessage()));
        }
        catch (ExpiredJwtException e){
            log.info("validateRefreshToken -> ExpiredJwtException");
            throw new JwtAuthenticationException(new APIError(CommonErrors.REFRESH_TOKEN_EXPIRED.toString(),CommonErrors.REFRESH_TOKEN_EXPIRED.getMessage()));
        } catch (IllegalArgumentException e){
            log.info("validateRefreshToken -> IllegalArgumentException");
            throw new JwtAuthenticationException(new APIError(CommonErrors.INVALID_REFRESH_TOKEN.toString(),CommonErrors.INVALID_REFRESH_TOKEN.getMessage()));
        }
        catch (UnsupportedJwtException e){
            log.info("validateRefreshToken -> UnsupportedJwtException");
            throw new JwtAuthenticationException(new APIError(CommonErrors.INVALID_REFRESH_TOKEN.toString(),CommonErrors.INVALID_REFRESH_TOKEN.getMessage()));
        }
        catch (Exception e){
            log.info("validateRefreshToken -> Exception");
            throw new JwtAuthenticationException(new APIError(CommonErrors.INVALID_REFRESH_TOKEN.toString(),CommonErrors.INVALID_REFRESH_TOKEN.getMessage()));
        }
    }
}
