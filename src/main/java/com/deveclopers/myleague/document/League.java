package com.deveclopers.myleague.document;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Getter
@Setter
public class League {

  @Id private String leagueId;
  private String name;
  private String location;
  private List<String> fields;
  private String major;
  @Deprecated private List<ObjectId> teams = new ArrayList<>();
  private String picture;
  @Deprecated private List<Phase> phases;
  @Deprecated private List<Statistic> statistics;
  @Deprecated private Positions positions;
  @Deprecated @DocumentReference private List<Round> rounds;
  private Boolean hasStarted;
  private Boolean hasFinished;
  private ObjectId activePhaseId;
  private ObjectId activeRoundId;
  private ObjectId userOwner;
  private List<ObjectId> editors;
}
