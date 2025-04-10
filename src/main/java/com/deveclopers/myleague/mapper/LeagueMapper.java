package com.deveclopers.myleague.mapper;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.LeagueDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LeagueMapper {

  LeagueMapper INSTANCE = Mappers.getMapper(LeagueMapper.class);

  League dtoToLeague(LeagueDto leagueDto);

  @Mapping(source = "leagueId", target = "id")
  DefaultDto instanceToDefaultDto(League league);
}
