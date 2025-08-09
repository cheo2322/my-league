package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.dto.RoundDto;
import com.deveclopers.myleague.service.PhaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/my_league/v1/phases")
@Slf4j
@CrossOrigin(origins = {"*"})
public class PhaseController {

  private final PhaseService phaseService;

  public PhaseController(PhaseService phaseService) {
    this.phaseService = phaseService;
  }

  @GetMapping("/{id}/rounds")
  @ResponseStatus(HttpStatus.OK)
  public Flux<RoundDto> getRoundsById(@PathVariable("id") String phaseId) {
    return phaseService.getRoundsByPhaseId(phaseId);
  }
}
