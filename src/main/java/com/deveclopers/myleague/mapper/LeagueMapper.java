package com.deveclopers.myleague.mapper;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.LeagueDto;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LeagueMapper {

  LeagueMapper INSTANCE = Mappers.getMapper(LeagueMapper.class);

  @Mapping(source = "activePhaseId", target = "activePhaseId", qualifiedByName = "stringToObjectId")
  @Mapping(source = "activeRoundId", target = "activeRoundId", qualifiedByName = "stringToObjectId")
  @Mapping(source = "isTheOwner", target = ".", ignore = true)
  League dtoToLeague(LeagueDto leagueDto);

  @Mapping(source = "leagueId", target = "id")
  DefaultDto instanceToDefaultDto(League league);

  @Mapping(source = "league.leagueId", target = "id")
  @Mapping(
      source = "league.activePhaseId",
      target = "activePhaseId",
      qualifiedByName = "objectIdToString")
  @Mapping(
      source = "league.activeRoundId",
      target = "activeRoundId",
      qualifiedByName = "objectIdToString")
  @Mapping(source = "isTheOwner", target = "isTheOwner")
  LeagueDto instanceToDto(League league, boolean isTheOwner);

  @Named("stringToObjectId")
  default ObjectId stringToObjectId(String id) {
    return id != null ? new ObjectId(id) : null;
  }

  @Named("objectIdToString")
  default String objectIdToString(ObjectId id) {
    return id != null ? id.toHexString() : "";
  }
}
