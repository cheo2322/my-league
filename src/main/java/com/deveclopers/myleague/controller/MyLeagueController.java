package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.service.MyLeagueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my_league/v1")
@Slf4j
@CrossOrigin(origins = {"*"})
public class MyLeagueController {

  private final MyLeagueService myLeagueService;

  public MyLeagueController(MyLeagueService myLeagueService) {
    this.myLeagueService = myLeagueService;
  }

  @PostMapping("/league")
  public ResponseEntity<String> postLeague() {
    return new ResponseEntity<>(myLeagueService.createLeague(), HttpStatus.OK);
  }
}
