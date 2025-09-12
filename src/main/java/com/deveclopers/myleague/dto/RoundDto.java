package com.deveclopers.myleague.dto;

import java.util.List;

// TODO: Remove misassigned responsibilities
public record RoundDto(
    String roundId,
    String leagueId,
    String leagueName,
    String phaseId,
    String phase,
    int order,
    List<MatchDto> matches) {}
