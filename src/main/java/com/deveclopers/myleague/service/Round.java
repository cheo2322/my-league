package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.Match;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class MatchDay {

  @Id
  private String matchDayId;


  private List<Match> matches;
}
