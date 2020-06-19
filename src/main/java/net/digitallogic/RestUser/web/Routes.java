package net.digitallogic.RestUser.web;

public class Routes {
    public static final String BASE = "/api";
    public static final String USERS = "/users";
    public static final String ROLES = "/roles";
    public static final String AUTHORITIES = "/authorities";

    public static final String USER_ROUTE = BASE + USERS;
    public static final String LOGIN_ROUTE = USER_ROUTE + "/login";
    public static final String ROLE_ROUTE = BASE + ROLES;
    public static final String AUTHORITY_ROUTE = BASE + AUTHORITIES;

}
