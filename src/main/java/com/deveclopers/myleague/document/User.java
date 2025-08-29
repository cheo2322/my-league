package com.deveclopers.myleague.document;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class User {

  @Id private String id;

  // TODO: Add in v2
  //  @Indexed(unique = true)
  private String username;

  @Indexed(unique = true)
  private String email;

  private String passwordHash;
  private Role globalRole = Role.USER;
  private LocalDateTime creationDate = LocalDateTime.now();

  public enum Role {
    ADMIN_MASTER,
    USER
  }
}
