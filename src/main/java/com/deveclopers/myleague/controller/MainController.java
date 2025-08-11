package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.dto.RoundDto;
import com.deveclopers.myleague.service.MainService;
import com.deveclopers.myleague.service.RoundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/my_league/v1/main")
@Slf4j
@CrossOrigin(origins = {"*"})
public class MainController {

  private final MainService mainService;

  public MainController(RoundService roundService, MainService mainService) {
    this.mainService = mainService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<RoundDto> getMainPage() {
    return mainService.getMainPage();
  }
}
