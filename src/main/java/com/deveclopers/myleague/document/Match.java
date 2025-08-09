package com.deveclopers.myleague.document;

import com.deveclopers.myleague.utils.BinaryList;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Match {

  @Id private String matchId;

  /** Reference {@link Round} */
  private ObjectId roundId;

  /** Reference: {@link :Team} */
  private ObjectId homeTeam;

  /** Reference: {@link :Team} */
  private ObjectId visitTeam;

  private Integer homeResult;
  private Integer visitResult;
  private Boolean hasExtraTime = false; // TODO: Add result
  private Boolean hasPenalties = false;
  private BinaryList homePenalties;
  private BinaryList visitantPenalties;
  private MatchStatus status;
  private LocalDateTime matchTime;
}
