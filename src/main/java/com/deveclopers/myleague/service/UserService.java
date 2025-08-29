package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.User;
import com.deveclopers.myleague.document.User.Role;
import com.deveclopers.myleague.dto.LoginDto;
import com.deveclopers.myleague.dto.UserDto;
import com.deveclopers.myleague.repository.UserRepository;
import com.deveclopers.myleague.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final JwtService jwtService;

  public UserService(
      UserRepository userRepository, PasswordEncoder encoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.encoder = encoder;
    this.jwtService = jwtService;
  }

  public Mono<User> registerUser(UserDto userDto) {
    return userRepository
        .findByEmail(userDto.email())
        .flatMap(existing -> Mono.<User>error(new RuntimeException("Already registered email.")))
        .switchIfEmpty(
            userRepository.save(
                new User(
                    null,
                    userDto.username(),
                    userDto.email(),
                    encoder.encode(userDto.password()),
                    Role.USER)));
  }

  public Mono<String> login(LoginDto dto) {
    return userRepository
        .findByEmail(dto.email())
        .switchIfEmpty(Mono.error(new RuntimeException("Not Found user.")))
        .flatMap(
            user -> {
              if (!encoder.matches(dto.password(), user.getPasswordHash())) {
                return Mono.error(new RuntimeException("Invalid credentials."));
              }

              String token = jwtService.generateToken(user);
              return Mono.just(token);
            });
  }
}
