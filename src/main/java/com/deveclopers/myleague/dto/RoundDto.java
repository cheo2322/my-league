package com.deveclopers.myleague.dto;

import java.util.List;

public record RoundDto(
    String id,
    String leagueId,
    String leagueName,
    String phaseId,
    String phase,
    Integer order,
    List<MatchDto> matches) {}
