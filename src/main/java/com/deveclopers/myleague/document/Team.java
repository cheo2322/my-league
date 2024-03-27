package com.deveclopers.myleague.document;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Team {

  @Id
  private String teamId;
  private String name;
  private List<Player> players;
  private List<String> pictures;
  private String major;
  private String abbreviation;
}
