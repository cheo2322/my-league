package com.deveclopers.myleague.document.v2;

import com.deveclopers.myleague.document.Position.PositionStatus;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class PositionV2 {
  private ObjectId teamId;
  private int position;
  private PositionStatus positionStatus;
  private int playedGames;
  private int points;
  private int favorGoals;
  private int againstGoals;
  private int goals;

  public PositionV2(ObjectId teamId) {
    this.teamId = teamId;
    this.position = 0;
    this.positionStatus = PositionStatus.KEEP;
    this.playedGames = 0;
    this.points = 0;
    this.favorGoals = 0;
    this.againstGoals = 0;
    this.goals = 0;
  }

  public void addFavorGoals(int newFavorGoals) {
    this.favorGoals += newFavorGoals;

    this.calculateGoals();
  }

  public void addAgainstGoals(int newAgainstGoals) {
    this.againstGoals += newAgainstGoals;

    this.calculateGoals();
  }

  private void calculateGoals() {
    this.goals = this.favorGoals - this.againstGoals;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PositionV2 that)) return false;
    return teamId.equals(that.teamId);
  }

  @Override
  public int hashCode() {
    return teamId.hashCode();
  }
}
