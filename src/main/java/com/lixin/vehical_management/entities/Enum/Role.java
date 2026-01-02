package com.lixin.vehical_management.entities.Enum;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum Role {
    /**
     * temp disable role CONTRIBUTOR and MODERATOR
     */
    USER("User", "user", "user"),
    ADMINISTRATOR("Administrator", "admin", "admin");

    public final String displayValue;
    public final String shortName;
    private final String value;

    public static final Map<String, Role> ROLE_MAP;

    Role(String display, String shortName, String value) {
        this.displayValue = display;
        this.shortName = shortName;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    static {
        ROLE_MAP = Arrays.stream(Role.values()).collect(Collectors.toMap(Role::getShortName, r -> r, (k1, k2) -> k1, HashMap::new));
    }

    public String getShortName() {
        return this.shortName;
    }

    public static Role getByShortName(String shortName) {
        return ROLE_MAP.get(shortName);
    }

    public static Role fromShortName(String shortName) {

        switch (shortName) {
            case "user":
                return USER;
            case "admin":
                return ADMINISTRATOR;
            default:
                throw new IllegalArgumentException("Short[" + shortName + "] not supported.");
        }
    }
}
