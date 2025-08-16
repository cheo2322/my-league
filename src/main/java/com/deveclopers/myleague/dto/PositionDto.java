package com.deveclopers.myleague.dto;

public record PositionDto(
    String team,
    String status,
    int playedGames,
    int points,
    int favorGoals,
    int againstGoals,
    String goals) {}
