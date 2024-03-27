package com.deveclopers.myleague.document;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Phase {

  @Id
  private String phaseId;
  private PhaseType phaseType;
  private List<Result> results;
  private Map<Integer, Team> positions;

  enum PhaseType {
    GROUP,
    SINGLE_MATCH,
    DOUBLE_MATCH
  }
}
