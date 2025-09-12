package com.deveclopers.myleague.security;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticatedUserContext {

  /**
   * Validates if the League's owner matches with the user context.
   *
   * @param ownerId from document to validate. Do not confuse with the current user ID.
   * @return True if the owner matches, False otherwise.
   */
  public Mono<Boolean> validateOwnership(String ownerId) {
    return this.getUserId().map(userId -> userId.equals(ownerId));
  }

  /**
   * Get the user ID from the context.
   *
   * @return The user ID.
   */
  public Mono<String> getUserId() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .filter(auth -> auth != null && auth.getPrincipal() instanceof String)
        .map(auth -> (String) auth.getPrincipal())
        .defaultIfEmpty("");
  }
}
