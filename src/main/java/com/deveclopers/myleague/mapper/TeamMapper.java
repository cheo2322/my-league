package com.deveclopers.myleague.mapper;

import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.TeamDto;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TeamMapper {

  TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

  @Mapping(source = "leagueId", target = "leagueId", qualifiedByName = "objectIdToString")
  TeamDto instanceToDto(Team team);

  @Mapping(source = "leagueId", target = "leagueId", qualifiedByName = "stringToObjectId")
  Team dtoToTeam(TeamDto teamDto);

  DefaultDto instanceToDefaultDto(Team team);

  @Named("stringToObjectId")
  default ObjectId stringToObjectId(String id) {
    return id != null ? new ObjectId(id) : null;
  }

  @Named("objectIdToString")
  default String objectIdToString(ObjectId id) {
    return id != null ? id.toHexString() : null;
  }
}
