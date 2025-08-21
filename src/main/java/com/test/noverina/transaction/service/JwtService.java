package com.test.noverina.transaction.service;

import com.test.noverina.transaction.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expiration;
    @Autowired
    private UserRepository repo;

    private SecretKey key() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        var entity = repo.findByUsername(username).orElseThrow();
        var expirationUnix = Instant.now().getEpochSecond() + expiration;
        return Jwts.builder()
                .subject(entity.getUsername())
                .issuer("TRANSACTION-SERVICE")
                .issuedAt(new Date())
                .expiration(Date.from(Instant.ofEpochSecond(expirationUnix)))
                .claim("role", List.of("ROLE_" + entity.getRole()))
                .claim("userId", entity.getUserId())
                .signWith(key())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        var claims = validateAndGetClaims(token);
        var entity = repo.findByUsername(claims.getSubject()).orElseThrow();

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("role");
        var authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new UsernamePasswordAuthenticationToken(entity, token, authorities);
    }

    private Claims validateAndGetClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}