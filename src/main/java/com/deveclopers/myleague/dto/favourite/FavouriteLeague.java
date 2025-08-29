package com.deveclopers.myleague.dto.favourite;

import com.deveclopers.myleague.dto.RoundDto;

public record FavouriteLeague(String userId, String name, boolean hasStarted, RoundDto roundDto) {}
