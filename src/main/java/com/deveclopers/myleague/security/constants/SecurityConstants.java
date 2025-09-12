package com.deveclopers.myleague.security.constants;

public class SecurityConstants {

  public static final String[] PUBLIC_POST = {"/my_league/v1/auth/*"};

  public static final String[] PUBLIC_GET = {
    "/my_league/v1/leagues/*/**", "/my_league/v1/matches/**", "/my_league/v1/teams/**"
  };

  public static final String[] AUTHENTICATED_POST = {
    "/my_league/v1/leagues/**", "/my_league/v1/main/**",
  };

  public static final String[] AUTHENTICATED_GET = {"/my_league/v1/leagues"};

  public static final String[] ADMIN_PATHS = {"/my_league/v1/leagues/*/positions/**"};
}
