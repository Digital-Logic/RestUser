package net.digitallogic.RestUser.security;

public enum Role {

    USER("USER"),
    ADMIN("ADMIN")
    ;

    public final String name;
    private Role(String name) { this.name = name; }
}
