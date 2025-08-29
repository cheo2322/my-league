package com.deveclopers.myleague.dto.favourite;

import java.util.List;

public record FavouriteDto(
    String userId, List<FavouriteLeague> leagues, List<FavouriteTeam> teams) {}
