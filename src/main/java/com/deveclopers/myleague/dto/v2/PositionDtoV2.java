package com.deveclopers.myleague.dto.v2;

public record PositionDtoV2(
    int position,
    String team,
    String status,
    int playedGames,
    int points,
    int favorGoals,
    int againstGoals,
    String goals) {}
