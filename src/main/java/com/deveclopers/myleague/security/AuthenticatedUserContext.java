package com.deveclopers.myleague.security;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticatedUserContext {

  public Mono<String> getUserId() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .filter(auth -> auth != null && auth.getPrincipal() instanceof String)
        .map(auth -> (String) auth.getPrincipal())
        .defaultIfEmpty("");
  }
}
