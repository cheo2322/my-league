package com.deveclopers.myleague.security;

import java.util.List;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public @NonNull Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      try {
        String userId = jwtService.extractUserId(token);

        Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, List.of());
        return chain
            .filter(exchange)
            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

      } catch (Exception e) {
        return Mono.error(new RuntimeException("Invalid JWT"));
      }
    }

    return chain.filter(exchange);
  }
}
