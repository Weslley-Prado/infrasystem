package br.com.dagostini.infrasystem.shared.domain.auth.model;

import lombok.Builder;

import java.util.List;

@Builder
public record TokenInfo(String userId, List<String> roles, String issuer) {
}
