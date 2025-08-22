package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.dto.LoginDto;
import com.deveclopers.myleague.dto.UserDto;
import com.deveclopers.myleague.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/my_league/v1/auth")
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public Mono<ResponseEntity<String>> register(@RequestBody UserDto userDto) {
    return userService
        .registerUser(userDto)
        .map(user -> ResponseEntity.ok("User registered."))
        .onErrorResume(
            e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage())));
  }

  @PostMapping("/login")
  public Mono<ResponseEntity<String>> login(@RequestBody LoginDto loginDto) {
    return userService
        .login(loginDto)
        .map(ResponseEntity::ok)
        .onErrorResume(
            e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage())));
  }
}
