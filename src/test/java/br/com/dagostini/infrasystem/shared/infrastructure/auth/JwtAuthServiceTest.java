package br.com.dagostini.infrasystem.shared.infrastructure.auth;

import br.com.dagostini.infrasystem.shared.domain.auth.model.TokenInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.security.auth.message.AuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthServiceTest {

    @InjectMocks
    private JwtAuthService jwtAuthService;

    private Key signingKey;
    private final String SECRET_KEY = "mySuperSecretKey12345678901234567890";
    private final String USER_ID = "user123";
    private final List<String> ROLES = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
    private final String ISSUER = "test-issuer";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtAuthService, "secretKey", SECRET_KEY);
        signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    @Test
    void testValidateToken_Successful() throws AuthException {
        String token = Jwts.builder()
                .setSubject(USER_ID)
                .claim("roles", ROLES)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        TokenInfo result = jwtAuthService.validateToken(token);

        assertNotNull(result);
        assertEquals(USER_ID, result.userId());
        assertEquals(ROLES, result.roles());
        assertEquals(ISSUER, result.issuer());
    }

    @Test
    void testValidateToken_NoRoles() throws AuthException {
        String token = Jwts.builder()
                .setSubject(USER_ID)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        TokenInfo result = jwtAuthService.validateToken(token);

        assertNotNull(result);
        assertEquals(USER_ID, result.userId());
        assertEquals(Collections.emptyList(), result.roles());
        assertEquals(ISSUER, result.issuer());
    }

    @Test
    void testValidateToken_ExpiredToken() {
        String token = Jwts.builder()
                .setSubject(USER_ID)
                .claim("roles", ROLES)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000)) //-2hrs
                .setExpiration(new Date(System.currentTimeMillis() - 3600000)) //-1hr
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        AuthException exception = assertThrows(AuthException.class, () -> {
            jwtAuthService.validateToken(token);
        });

        assertEquals("Invalid or expired JWT token", exception.getMessage());
    }

    @Test
    void testValidateToken_InvalidSignature() {
        Key wrongKey = Keys.hmacShaKeyFor("differentSecretKey12345678901234567890".getBytes());
        String token = Jwts.builder()
                .setSubject(USER_ID)
                .claim("roles", ROLES)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(wrongKey, SignatureAlgorithm.HS256)
                .compact();

        AuthException exception = assertThrows(AuthException.class, () -> {
            jwtAuthService.validateToken(token);
        });

        assertEquals("Invalid or expired JWT token", exception.getMessage());
    }

    @Test
    void testValidateToken_MalformedToken() {
        String malformedToken = "invalid.token.string";

        AuthException exception = assertThrows(AuthException.class, () -> {
            jwtAuthService.validateToken(malformedToken);
        });

        assertEquals("Invalid or expired JWT token", exception.getMessage());
    }

    @Test
    void testValidateToken_NullToken() {
        AuthException exception = assertThrows(AuthException.class, () -> {
            jwtAuthService.validateToken(null);
        });

        assertEquals("Invalid or expired JWT token", exception.getMessage());
    }

    @Test
    void testValidateToken_EmptyToken() {
        AuthException exception = assertThrows(AuthException.class, () -> {
            jwtAuthService.validateToken("");
        });

        assertEquals("Invalid or expired JWT token", exception.getMessage());
    }
}