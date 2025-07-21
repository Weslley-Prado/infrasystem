package br.com.dagostini.infrasystem.security; // Adjust package as needed

import br.com.dagostini.infrasystem.shared.domain.auth.model.TokenInfo;
import br.com.dagostini.infrasystem.shared.domain.auth.service.AuthService;
import br.com.dagostini.infrasystem.shared.interfaces.filter.JwtAuthenticationFilter;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private AuthService authService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private static final String VALID_TOKEN = "valid-jwt-token";
    private static final String INVALID_TOKEN = "invalid-jwt-token";
    private static final String USER_ID = "user123";
    private static final List<String> ROLES = List.of("USER", "ADMIN");

    private record TokenInfo(String userId, List<String> roles) {}

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldAuthenticateWithValidToken() throws ServletException, IOException, AuthException {
        request.addHeader("Authorization", "Bearer " + VALID_TOKEN);
        when(authService.validateToken(VALID_TOKEN)).thenReturn(new br.com.dagostini.infrasystem.shared.domain.auth.model.TokenInfo(USER_ID, ROLES, ""));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(200, response.getStatus());
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authToken);
        UserDetails userDetails = (UserDetails) authToken.getPrincipal();
        assertEquals(USER_ID, userDetails.getUsername());
        assertEquals("", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        verify(authService, times(1)).validateToken(VALID_TOKEN);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticateWithoutAuthorizationHeader() throws ServletException, IOException, AuthException {
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(200, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(authService, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticateWithInvalidHeaderFormat() throws ServletException, IOException, AuthException {
        request.addHeader("Authorization", "InvalidToken");
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(200, response.getStatus()); // No error status set
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(authService, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldReturnUnauthorizedForInvalidToken() throws ServletException, IOException, AuthException {
        request.addHeader("Authorization", "Bearer " + INVALID_TOKEN);
        when(authService.validateToken(INVALID_TOKEN)).thenThrow(new RuntimeException("Invalid token"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());
        assertEquals("Invalid JWT token", response.getContentAsString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(authService, times(1)).validateToken(INVALID_TOKEN);
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void doFilterInternal_shouldClearSecurityContextBeforeSettingNewAuthentication() throws ServletException, IOException, AuthException {
        request.addHeader("Authorization", "Bearer " + VALID_TOKEN);
        when(authService.validateToken(VALID_TOKEN)).thenReturn(new br.com.dagostini.infrasystem.shared.domain.auth.model.TokenInfo(USER_ID, ROLES, ""));

        UserDetails previousUser = User.withUsername("previousUser").password("").roles("GUEST").build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(previousUser, null, previousUser.getAuthorities())
        );

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authToken);
        UserDetails userDetails = (UserDetails) authToken.getPrincipal();
        assertEquals(USER_ID, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertFalse(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GUEST")));
        verify(authService, times(1)).validateToken(VALID_TOKEN);
        verify(filterChain, times(1)).doFilter(request, response);
    }
}