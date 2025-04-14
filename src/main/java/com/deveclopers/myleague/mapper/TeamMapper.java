package com.deveclopers.myleague.mapper;

import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.TeamDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TeamMapper {

  TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

  Team dtoToTeam(TeamDto teamDto);

  TeamDto instanceToDto(Team team);
}
