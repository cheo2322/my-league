package com.deveclopers.myleague.document;

import com.deveclopers.myleague.utils.BinaryList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@ToString
public class Match {

  @Id
  private String matchId;
  private Team home;
  private Team visitant;
  private Integer homeResult;
  private Integer visitantResult;
  private Boolean hasExtraTime;
  private Boolean hasPenalties;
  private BinaryList homePenalties;
  private BinaryList visitantPenalties;
  private Team winner;
  private Boolean hasFinished;
}
