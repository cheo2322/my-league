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
public class Result {

  @Id
  private String resultId;
  private Team home;
  private Team visitant;
  private Double homeResult;
  private Double visitantResult;
  private Boolean hasExtraTime;
  private Double homeExtraTimeResult;
  private Double visitantExtraTimeResult;
  private Boolean hasPenalties;
  private BinaryList homePenalties;
  private BinaryList visitantPenalties;
  private Team winner;
}
