package com.deveclopers.myleague.document;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class Position {
  private ObjectId teamId;
  private int playedGames;
  private int points;
  private int favorGoals;
  private int againstGoals;
  private int goals;

  public Position(String teamId) {
    this.teamId = new ObjectId(teamId);
    playedGames = 0;
    points = 0;
    favorGoals = 0;
    againstGoals = 0;
    goals = 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Position that)) return false;
    return teamId.equals(that.teamId);
  }

  @Override
  public int hashCode() {
    return teamId.hashCode();
  }
}
