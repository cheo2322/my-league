package com.deveclopers.myleague.dto;

import java.util.List;

public record PositionsDto(String id, int round, List<PositionDto> positions) {}
