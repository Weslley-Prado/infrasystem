package br.com.dagostini.infrasystem.shared.infrastructure.auth;

import br.com.dagostini.infrasystem.shared.domain.auth.model.TokenInfo;
import br.com.dagostini.infrasystem.shared.domain.auth.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class JwtAuthService implements AuthService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public TokenInfo validateToken(String token) throws AuthException {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            List<String> roles = claims.get("roles", List.class);
            if (roles == null) {
                log.warn("No roles found in token for user: {}", userId);
                roles = Collections.emptyList();
            }
            String issuer = claims.getIssuer();
            log.info("Token validated: userId={}, roles={}, issuer={}", userId, roles, issuer);

            return new TokenInfo(userId, roles, issuer);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage(), e);
            throw new AuthException("Invalid or expired JWT token", e);
        }
    }
}