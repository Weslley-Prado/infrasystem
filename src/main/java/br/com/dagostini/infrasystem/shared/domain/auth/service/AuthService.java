package br.com.dagostini.infrasystem.shared.domain.auth.service;

import br.com.dagostini.infrasystem.shared.domain.auth.model.TokenInfo;
import jakarta.security.auth.message.AuthException;

public interface AuthService {
    TokenInfo validateToken(String token) throws AuthException;
}
