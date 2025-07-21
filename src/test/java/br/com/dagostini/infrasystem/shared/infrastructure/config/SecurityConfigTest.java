package br.com.dagostini.infrasystem.shared.infrastructure.config;

import br.com.dagostini.infrasystem.shared.interfaces.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Mock
    private HttpSecurity httpSecurity;

    @BeforeEach
    void setUp() {
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }

    @Test
    void securityFilterChain_shouldConfigureCorrectly() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), eq(UsernamePasswordAuthenticationFilter.class))).thenReturn(httpSecurity);
        when(httpSecurity.exceptionHandling(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        SecurityFilterChain filterChain = securityConfig.securityFilterChain(httpSecurity);

        assertNotNull(filterChain);
        verify(httpSecurity, times(1)).csrf(any());
        verify(httpSecurity, times(1)).sessionManagement(any());
        verify(httpSecurity, times(1)).authorizeHttpRequests(any());
        verify(httpSecurity, times(1)).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        verify(httpSecurity, times(1)).exceptionHandling(any());
        verify(httpSecurity, times(1)).build();
    }

    @Test
    void securityFilterChain_shouldConfigureUnauthorizedExceptionHandling() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), eq(UsernamePasswordAuthenticationFilter.class))).thenReturn(httpSecurity);
        when(httpSecurity.exceptionHandling(any())).thenAnswer(invocation -> {
            return httpSecurity;
        });
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        SecurityFilterChain filterChain = securityConfig.securityFilterChain(httpSecurity);

        assertNotNull(filterChain);
        verify(httpSecurity, times(1)).exceptionHandling(any());
        verify(httpSecurity, times(1)).build();
    }
}