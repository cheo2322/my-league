package com.deveclopers.myleague.mapper;

import com.deveclopers.myleague.document.User;
import com.deveclopers.myleague.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "username", ignore = true) // TODO: Add in v2
  @Mapping(target = "globalRole", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "favouriteLeagues", ignore = true)
  @Mapping(target = "favouriteTeams", ignore = true)
  User dtoToInstance(UserDto userDto);
}
