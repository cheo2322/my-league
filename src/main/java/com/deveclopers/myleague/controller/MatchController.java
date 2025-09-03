package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.dto.MatchDetailsDto;
import com.deveclopers.myleague.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/my_league/v1/matches")
@Slf4j
@CrossOrigin(origins = {"*"})
public class MatchController {

  private final MatchService matchService;

  public MatchController(MatchService matchService) {
    this.matchService = matchService;
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseEntity<String>> updateMatchResult(
      @PathVariable("id") String matchId,
      @RequestParam int homeResult,
      @RequestParam int visitResult) {

    return matchService
        .updateMatchResult(matchId, homeResult, visitResult)
        .thenReturn(ResponseEntity.ok("OK"))
        .onErrorResume(
            e ->
                Mono.just(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("PATCH error: " + e.getMessage())));
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseEntity<MatchDetailsDto>> getMatchDetails(@PathVariable("id") String matchId) {
    return matchService.getMatchDetails(matchId).map(ResponseEntity::ok);
  }
}
