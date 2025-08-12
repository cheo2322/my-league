package com.deveclopers.myleague.dto;

import java.util.List;

public record RoundDto(
    String roundId,
    String leagueId,
    String leagueName,
    String phaseId,
    String phase,
    int order,
    List<MatchDto> matches) {}
