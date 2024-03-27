package com.deveclopers.myleague.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Player {

  @Id
  private String playerId;
  private String name;
  private Integer age;
  private Integer number;
  private String nickname;
  private Position position;
  private Double height;
  private HeightType heightType;
  private String picture;

  enum Position {
    GK,
    DEF,
    MED,
    DEL
  }

  enum HeightType {
    MT,
    FEET
  }
}
