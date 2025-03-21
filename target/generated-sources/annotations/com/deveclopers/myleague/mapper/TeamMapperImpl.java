package com.deveclopers.myleague.mapper;

import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.TeamDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-21T12:51:29-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class TeamMapperImpl implements TeamMapper {

    @Override
    public Team dtoToTeam(TeamDto teamDto) {
        if ( teamDto == null ) {
            return null;
        }

        Team team = new Team();

        team.setName( teamDto.getName() );
        team.setMajor( teamDto.getMajor() );
        team.setAbbreviation( teamDto.getAbbreviation() );

        return team;
    }
}
