package com.lixin.vehical_management.entities.Enum;

public enum EntryExitType {
    IN("IN"),
    OUT("OUT");

    public final String name;

    EntryExitType(String name) {
        this.name = name;
    }

    public static EntryExitType of(String name) {
        if (name == null) {
            return null;
        }

        for (EntryExitType value : EntryExitType.values()) {
            if (name.equalsIgnoreCase(value.name)) {
                return value;
            }
        }

        return null;
    }
}
