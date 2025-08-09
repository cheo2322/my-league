package com.deveclopers.myleague.utils;

import com.deveclopers.myleague.document.Round;
import java.util.List;

public class LeagueUtil {

  public static Round getRoundFromOrder(List<Round> rounds, Integer order) {
    for (int i = 0; i < rounds.size(); i++) {
      if (rounds.get(i).getOrder().equals(order)) {
        return rounds.get(i);
      }
    }

    throw new RuntimeException();
  }
}
