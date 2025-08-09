package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.dto.MatchDto;
import com.deveclopers.myleague.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/my_league/v1/matches")
@Slf4j
@CrossOrigin(origins = {"*"})
public class MatchController {

  private final MatchService matchService;

  public MatchController(MatchService matchService) {
    this.matchService = matchService;
  }

  // TODO: It must depend on each user
  @Deprecated
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<MatchDto> getMatches() {
    return matchService.getMatches();
  }
}
