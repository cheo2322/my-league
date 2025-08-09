package com.deveclopers.myleague.dto;

import java.util.List;

public record RoundDto(String id, Integer order, String phaseId, List<MatchDto> matches) {}
