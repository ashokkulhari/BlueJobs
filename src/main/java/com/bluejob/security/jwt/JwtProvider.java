package com.bluejob.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Arrays;
import io.jsonwebtoken.security.Keys;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.bluejob.security.services.UserPrinciple;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import java.security.Key;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${shophere.app.base64-secret}")
    private String jwtSecret;

    @Value("${shophere.app.jwtExpiration}")
    private int jwtExpiration;

    private static final String AUTHORITIES_KEY = "auth";
    
    private Key key;
    @PostConstruct
    public void init() {
        byte[] keyBytes;
        logger.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        
    }
    
    public String generateJwtToken(Authentication authentication) {

//        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        System.out.println("generate JWT token authorities = "+authorities +" authentication.getName()= "+authentication.getName());
        
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000))
                .compact();
        
//        return Jwts.builder()
//                .setSubject((userPrincipal.getUsername()))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000))
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        if (StringUtils.isBlank(authentication.getName())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("scopes", authorities);

        String token = Jwts.builder()
          .setClaims(claims)
          .setId(UUID.randomUUID().toString())
//          .setIssuedAt(currentTime.toDate())
          .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000))
          .signWith(key, SignatureAlgorithm.HS512)
        .compact();

        return token;
    }
    
    public boolean validateJwtToken(String authToken) {
    	boolean isValid=false;
//        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            isValid=true;
//        } catch (SignatureException e) {
//            logger.error("Invalid JWT signature -> Message: {} ", e);
//        } catch (MalformedJwtException e) {
//            logger.error("Invalid JWT token -> Message: {}", e);
//        } catch (ExpiredJwtException e) {
//            logger.error("Expired JWT token -> Message: {}", e);
//        } catch (UnsupportedJwtException e) {
//            logger.error("Unsupported JWT token -> Message: {}", e);
//        } catch (IllegalArgumentException e) {
//            logger.error("JWT claims string is empty -> Message: {}", e);
//        }
        return isValid;    
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
    
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}