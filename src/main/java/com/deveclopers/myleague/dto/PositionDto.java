package com.deveclopers.myleague.dto;

public record PositionDto(
    String team, int playedGames, int points, int favorGoals, int againstGoals, String goals) {}
