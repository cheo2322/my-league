package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.Match;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Getter
@Setter
public class Round {

  public Round(int order) {
    this.order = order;
  }

  @Id
  private String matchDayId;
  @DocumentReference
  private List<Match> matches;
  private Integer order;
  private List<Date> dates;
}
