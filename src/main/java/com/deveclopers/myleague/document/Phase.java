package com.deveclopers.myleague.document;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Phase {

  @Id private String phaseId;

  /** Reference: {@link League} */
  private ObjectId leagueId;

  private String name;
  private PhaseType phaseType;
  private PhaseCycle phaseCycle;

  // TODO: Make enums to be part of a database's table (Epic 001)
  private enum PhaseType {
    LEAGUE,
    GROUP,
    PLAYOFF
  }

  enum PhaseCycle {
    ONE_MATCH,
    DOUBLE_MATCH
  }
}
