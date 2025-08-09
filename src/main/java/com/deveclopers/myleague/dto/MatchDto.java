package com.deveclopers.myleague.dto;

public record MatchDto(
    String id,
    String homeTeam,
    String visitTeam,
    Integer homeResult,
    Integer visitResult,
    String status,
    String date,
    String time) {}
