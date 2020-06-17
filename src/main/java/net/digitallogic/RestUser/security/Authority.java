package net.digitallogic.RestUser.security;

public enum Authority {

    CREATE_USER("CREATE_USER"),
    READ_USER("READ_USER"),
    UPDATE_USER("UPDATE_USER"),
    DELETE_USER("DELETE_USER"),

    CREATE_ROLE("CREATE_ROLE"),
    READ_ROLE("READ_ROLE"),
    UPDATE_ROLE("UPDATE_ROLE"),
    DELETE_ROLE("DELETE_ROLE"),

    READ_AUTHORITY("READ_AUTHORITY")
    ;

    public final String name;
    private Authority(String name) { this.name = name; }
}
