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
  League dtoToLeague(LeagueDto leagueDto);

  @Mapping(source = "leagueId", target = "id")
  DefaultDto instanceToDefaultDto(League league);

  @Mapping(source = "leagueId", target = "id")
  @Mapping(source = "activePhaseId", target = "activePhaseId", qualifiedByName = "objectIdToString")
  @Mapping(source = "activeRoundId", target = "activeRoundId", qualifiedByName = "objectIdToString")
  LeagueDto instanceToDto(League league);

  @Named("stringToObjectId")
  default ObjectId stringToObjectId(String id) {
    return id != null ? new ObjectId(id) : null;
  }

  @Named("objectIdToString")
  default String objectIdToString(ObjectId id) {
    return id != null ? id.toHexString() : "";
  }
}
