package com.deveclopers.myleague.document.v2;

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
public class PositionsV2 {

  @Id private String positionsId;

  private ObjectId leagueId;
  private ObjectId phaseId;
  private ObjectId roundId;
  private List<PositionV2> positions = new ArrayList<>();
}
