package com.lixin.vehical_management.entities.converter;

import com.lixin.vehical_management.entities.Enum.Role;
import jakarta.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role.shortName;
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return Role.fromShortName(dbData);
    }
}
