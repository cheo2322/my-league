package com.deveclopers.myleague.document;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Getter
@Setter
public class League {

  @Id
  private String leagueId;
  private String name;
  private String location;
  private String field;
  private String major;
  @DocumentReference
  private List<Team> teams;
  private String picture;
  private List<Phase> phases;
  private List<Statistic> statistics;
}
