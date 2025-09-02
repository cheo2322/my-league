package com.deveclopers.myleague.dto.v2;

import java.util.List;

public record PositionsDtoV2(String id, int round, List<PositionDtoV2> positions) {}
