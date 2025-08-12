package com.deveclopers.myleague.document;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Positions {

  @Id private String positionsId;

  private ObjectId leagueId;
  private ObjectId phaseId;
  private ObjectId roundId;
  private List<Position> positions = new ArrayList<>();
}
