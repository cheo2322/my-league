package com.deveclopers.myleague.mapper;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.dto.LeagueDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-30T00:27:52-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class LeagueMapperImpl implements LeagueMapper {

    @Override
    public League dtoToLeague(LeagueDto leagueDto) {
        if ( leagueDto == null ) {
            return null;
        }

        League league = new League();

        league.setName( leagueDto.getName() );
        league.setLocation( leagueDto.getLocation() );
        league.setField( leagueDto.getField() );
        league.setMajor( leagueDto.getMajor() );
        league.setPicture( leagueDto.getPicture() );

        return league;
    }
}
