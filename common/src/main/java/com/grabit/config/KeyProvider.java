package com.grabit.config;

import com.grabit.Utilities.Utility;
import com.grabit.exception.CustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@Log4j2
public class KeyProvider {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public KeyProvider() {
        try {
            this.privateKey = loadPrivateKey("keys/private_key.pem");
            this.publicKey = loadPublicKey("keys/public_key.pem");
        } catch (Exception e) {
            log.info("Something went wrong while loading PEM file : "+e);
            throw new CustomException(Utility.buildErrorObject("PEM_LOAD_FAILURE","Unable to load PEM file",500,"KeyProvider"));
        }
    }

    private PrivateKey loadPrivateKey(String path) throws Exception {
        InputStream inputStream = new ClassPathResource(path).getInputStream();
        String key = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String path) throws Exception {
        InputStream inputStream = new ClassPathResource(path).getInputStream();
        String key = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
