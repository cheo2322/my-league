package com.deveclopers.myleague.dto;

public record LeagueDto(
    String id, String name, String activePhaseId, String activeRoundId, boolean isTheOwner) {}
