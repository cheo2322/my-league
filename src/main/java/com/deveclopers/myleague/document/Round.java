package com.deveclopers.myleague.document;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Round {

  public Round(int order) {
    this.order = order;
  }

  @Id private String roundId;
  private Integer order;

  /** Reference: {@link Phase} */
  private ObjectId phaseId;
}
