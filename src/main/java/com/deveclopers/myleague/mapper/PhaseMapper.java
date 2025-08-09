package com.deveclopers.myleague.mapper;

import com.deveclopers.myleague.document.Phase;
import com.deveclopers.myleague.dto.PhaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PhaseMapper {

  PhaseMapper INSTANCE = Mappers.getMapper(PhaseMapper.class);

  @Mapping(source = "phaseId", target = "id")
  @Mapping(source = "phaseStatus", target = "status")
  PhaseDto instanceToDto(Phase phase);
}
