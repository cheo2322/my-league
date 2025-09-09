package com.deveclopers.myleague.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Field {

  @Id private String fieldId;

  private String name;
  private String location;
}
