package com.deveclopers.myleague.security;

import com.deveclopers.myleague.document.User.Role;
import com.deveclopers.myleague.security.constants.SecurityConstants;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.algorithm}")
  private String jwtAlgorithm;

  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http.csrf(CsrfSpec::disable)
        .authorizeExchange(configureAuthorization())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        .build();
  }

  @Bean
  public ReactiveJwtDecoder jwtDecoder() {
    SecretKey key =
        new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), this.jwtAlgorithm);
    return NimbusReactiveJwtDecoder.withSecretKey(key).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private Customizer<AuthorizeExchangeSpec> configureAuthorization() {
    return exchanges ->
        exchanges
            .pathMatchers(HttpMethod.POST, SecurityConstants.PUBLIC_POST)
            .permitAll()
            .pathMatchers(HttpMethod.GET, SecurityConstants.PUBLIC_GET)
            .permitAll()
            .pathMatchers(HttpMethod.POST, SecurityConstants.AUTHENTICATED_POST)
            .authenticated()
            .pathMatchers(SecurityConstants.ADMIN_PATHS)
            .hasRole(Role.ADMIN_MASTER.name())
            .anyExchange()
            .authenticated();
  }
}
