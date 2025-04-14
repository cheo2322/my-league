package com.deveclopers.myleague.document;

import com.deveclopers.myleague.service.Round;
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
  private List<ObjectId> teams;
  private String picture;
  private List<Phase> phases;
  private List<Statistic> statistics;
  private Positions positions;
  @DocumentReference private List<Round> rounds;
  private Boolean isComplete;
  private Boolean isStarted;
  private Boolean isFinished;
}
