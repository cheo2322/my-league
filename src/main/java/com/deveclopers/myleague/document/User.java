package com.deveclopers.myleague.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
public class User {

  @Id private String id;

  @Indexed(unique = true)
  private String username;

  private String email;
  private String passwordHash;
  private Role globalRole;

  public enum Role {
    ADMIN_MASTER,
    USER
  }
}
