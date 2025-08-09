package com.deveclopers.myleague.mapper;

import com.deveclopers.myleague.document.Match;
import com.deveclopers.myleague.dto.MatchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MatchMapper {

  MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

  @Mapping(source = "matchId", target = "id")
  @Mapping(target = "homeTeam", ignore = true)
  @Mapping(target = "visitTeam", ignore = true)
  MatchDto instanceToDto(Match match);
}
